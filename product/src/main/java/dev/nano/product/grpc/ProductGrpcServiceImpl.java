package dev.nano.product.grpc;

import dev.nano.exceptionhandler.core.ResourceNotFoundException;
import dev.nano.grpc.product.GetProductRequest;
import dev.nano.grpc.product.GetProductResponse;
import dev.nano.grpc.product.ProductGrpcServiceGrpc;
import dev.nano.product.ProductDTO;
import dev.nano.product.ProductService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

/** gRPC server — exposes ProductService for internal service-to-service calls. */
@GrpcService
@RequiredArgsConstructor
@Slf4j
public class ProductGrpcServiceImpl extends ProductGrpcServiceGrpc.ProductGrpcServiceImplBase {

    private final ProductService productService;

    @Override
    public void getProduct(GetProductRequest request,
                           StreamObserver<GetProductResponse> responseObserver) {

        long productId = request.getProductId();
        log.info("gRPC GetProduct — productId={}", productId);

        try {
            ProductDTO product = productService.getProduct(productId);

            responseObserver.onNext(GetProductResponse.newBuilder()
                    .setId(product.getId())
                    .setName(product.getName())
                    .setImage(product.getImage() != null ? product.getImage() : "")
                    .setPrice(product.getPrice())
                    .setAvailableQuantity(product.getAvailableQuantity())
                    .build());
            responseObserver.onCompleted();

        } catch (ResourceNotFoundException e) {
            responseObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        } catch (Exception e) {
            log.error("gRPC GetProduct failed — productId={}: {}", productId, e.getMessage(), e);
            responseObserver.onError(Status.INTERNAL.withDescription("Internal server error").asRuntimeException());
        }
    }
}

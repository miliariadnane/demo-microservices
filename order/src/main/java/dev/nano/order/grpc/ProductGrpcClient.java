package dev.nano.order.grpc;

import dev.nano.exceptionhandler.core.ResourceNotFoundException;
import dev.nano.grpc.product.GetProductRequest;
import dev.nano.grpc.product.GetProductResponse;
import dev.nano.grpc.product.ProductGrpcServiceGrpc;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductGrpcClient {

    @GrpcClient("product-grpc-service")
    private ProductGrpcServiceGrpc.ProductGrpcServiceBlockingStub productStub;

    public GetProductResponse getProduct(long productId) {
        try {
            return productStub.getProduct(
                    GetProductRequest.newBuilder().setProductId(productId).build()
            );
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.NOT_FOUND) {
                throw new ResourceNotFoundException("Product with ID " + productId + " not found");
            }
            log.error("gRPC call to product-service failed: status={}", e.getStatus().getCode());
            throw new RuntimeException("Failed to fetch product via gRPC: " + e.getStatus().getDescription(), e);
        }
    }
}

package dev.nano.customer.grpc;

import dev.nano.customer.CustomerDTO;
import dev.nano.customer.CustomerService;
import dev.nano.exceptionhandler.core.ResourceNotFoundException;
import dev.nano.grpc.customer.CustomerGrpcServiceGrpc;
import dev.nano.grpc.customer.GetCustomerRequest;
import dev.nano.grpc.customer.GetCustomerResponse;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

/** gRPC server — exposes CustomerService for internal service-to-service calls. */
@GrpcService
@RequiredArgsConstructor
@Slf4j
public class CustomerGrpcServiceImpl extends CustomerGrpcServiceGrpc.CustomerGrpcServiceImplBase {

    private final CustomerService customerService;

    @Override
    public void getCustomer(GetCustomerRequest request,
                            StreamObserver<GetCustomerResponse> responseObserver) {

        long customerId = request.getCustomerId();
        log.info("gRPC GetCustomer — customerId={}", customerId);

        try {
            CustomerDTO customer = customerService.getCustomer(customerId);

            responseObserver.onNext(GetCustomerResponse.newBuilder()
                    .setId(customer.getId())
                    .setName(customer.getName())
                    .setEmail(customer.getEmail())
                    .setPhone(customer.getPhone() != null ? customer.getPhone() : "")
                    .setAddress(customer.getAddress() != null ? customer.getAddress() : "")
                    .build());
            responseObserver.onCompleted();

        } catch (ResourceNotFoundException e) {
            responseObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        } catch (Exception e) {
            log.error("gRPC GetCustomer failed — customerId={}: {}", customerId, e.getMessage(), e);
            responseObserver.onError(Status.INTERNAL.withDescription("Internal server error").asRuntimeException());
        }
    }
}

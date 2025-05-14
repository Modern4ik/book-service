package com.library.book_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", path = "/api/v1/users")
public interface UserServiceClient {

    @GetMapping("/exists/{id}")
    boolean existsById(@PathVariable Long id);

}

package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.IncentiveResponse;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class IncentiveService {

    @Autowired
    private RestTemplate restTemplate;

    public float getIncentive(Transaction transaction) {
    String url = "http://localhost:8080/incentive";

    IncentiveResponse response =
        restTemplate.postForObject(url, transaction, IncentiveResponse.class);

    return response != null ? response.getAmount() : 0;
}
}





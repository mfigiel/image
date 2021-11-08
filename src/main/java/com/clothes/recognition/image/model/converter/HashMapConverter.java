package com.clothes.recognition.image.model.converter;

import com.amazonaws.services.rekognition.model.Label;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class HashMapConverter {

    public String convertToJson(List<Label> customerInfo) {
        ObjectMapper objectMapper = new ObjectMapper();
        String customerInfoJson = null;
        try {
            customerInfoJson = objectMapper.writeValueAsString(customerInfo);
        } catch (final JsonProcessingException e) {
            log.error("JSON writing error", e);
        }

        return customerInfoJson;
    }

    public List<Label> convertToObject(String customerInfoJSON) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Label> customerInfo = null;
        try {
            customerInfo = objectMapper.readValue(customerInfoJSON, List.class);
        } catch (final IOException e) {
            log.error("JSON reading error", e);
        }

        return customerInfo;
    }

}

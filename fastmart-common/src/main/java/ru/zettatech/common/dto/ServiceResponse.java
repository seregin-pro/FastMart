package ru.zettatech.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ServiceResponse<T> {

    private String code;
    private String message;
    private T data;

    public static ServiceResponse<String> success(String message) {
        ServiceResponse<String> response = new ServiceResponse<>();
        response.setCode("200");
        response.setMessage(message);
        return response;
    }

    public static <T> ServiceResponse<T> success(String code, String message, T data) {
        ServiceResponse<T> response = new ServiceResponse<>();
        response.setCode(code);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    public static ServiceResponse<String> error(String code, String message) {
        ServiceResponse<String> response = new ServiceResponse<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
}

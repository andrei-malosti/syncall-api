package com.syncall.api.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDTO {

    private String status;
    private String message;
    private String path;
    private List<String> fields;

    public ErrorResponseDTO (String status, String message, List<String> fields, String path){
        this.status = status;
        this.message = message;
        this.fields = fields;
        this.path = path;
    }

    public ErrorResponseDTO (String status, List<String> fields, String path){
        this.status = status;
        this.fields = fields;
        this.path = path;
    }

    public ErrorResponseDTO (String status, String message, String path){
        this.status = status;
        this.message = message;
        this.path = path;
    }

}

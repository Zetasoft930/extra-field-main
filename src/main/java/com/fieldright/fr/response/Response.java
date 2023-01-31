package com.fieldright.fr.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Response<T> {

    private HttpStatus status;
    private T data;
    private List<String> errors;

    public static class Builder {

        private Object data;
        private List<String> errors;
        private HttpStatus status;

        public Builder withData(Object data){
            this.data = data;
            return this;
        }

        public Builder withErrors(List<String> errors){
            this.errors = errors;
            return this;
        }

        public Builder withStatus(HttpStatus status){
            this.status = status;
            return this;
        }

        public Response build(){
            Response response = new Response<>();

            response.data = this.data;
            response.errors = this.errors;
            response.status = this.status;

            return response;
        }

    }

}

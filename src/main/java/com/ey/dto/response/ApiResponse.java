package com.ey.dto.response;
public class ApiResponse<T> {
   private boolean success;
   private T data;
   private String message;
   public ApiResponse(boolean success, T data) {
       this.success = success;
       this.data = data;
   }
   public ApiResponse(boolean success, String message) {
       this.success = success;
       this.message = message;
   }
   public boolean isSuccess() {
       return success;
   }
   public T getData() {
       return data;
   }
   public String getMessage() {
       return message;
   }
}
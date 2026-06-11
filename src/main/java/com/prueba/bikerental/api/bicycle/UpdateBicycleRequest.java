package com.prueba.bikerental.api.bicycle;

import com.prueba.bikerental.bicycle.BicycleStatus;

import jakarta.validation.constraints.NotNull;

public class UpdateBicycleRequest {
    
    @NotNull(message = "El estatus no puede ser nulo")
    private BicycleStatus status;

    // Constructor vacío requerido por Spring/Jackson
    public UpdateBicycleRequest() {}

    public UpdateBicycleRequest(BicycleStatus status) {
        this.status = status;
    }

    // GETTER (Obligatorio para que Spring pueda leer el dato)
    public BicycleStatus getStatus() {
        return status;
    }

    // SETTER (Obligatorio para que Spring pueda asignarle el dato del JSON)
    public void setStatus(BicycleStatus status) {
        this.status = status;
    }
}
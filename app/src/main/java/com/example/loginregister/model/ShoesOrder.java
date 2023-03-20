package com.example.loginregister.model;

public class ShoesOrder {
    private GenericShoes genericShoes;
    private int size;
    private int Quantity;

    public ShoesOrder() {
    }

    public ShoesOrder(GenericShoes genericShoes, int size, int quantity) {
        this.genericShoes = genericShoes;
        this.size = size;
        Quantity = quantity;
    }

    public GenericShoes getGenericShoes() {
        return genericShoes;
    }

    public void setGenericShoes(GenericShoes genericShoes) {
        this.genericShoes = genericShoes;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
}

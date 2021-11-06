package pl.com.seremak.service;

import io.vavr.collection.List;

public class testClass {

    public static void main(String[] args) {
        var lista = List.of(1, 2, 3, 4 ,5);
        System.out.println(lista);
        System.out.println(lista.prepend(888));
        System.out.println(lista.append(999));
    }
}

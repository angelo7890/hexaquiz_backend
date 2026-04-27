package com.hexaquiz.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "option")
public class OptionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;;

    private String text;

    private String image;


}

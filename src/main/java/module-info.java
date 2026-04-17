module PlanetMel  {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // Spring / Spring Boot
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.beans;
    requires spring.core;

    // JPA + H2
    requires spring.data.jpa;
    requires jakarta.persistence;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires java.prefs;
    requires spring.data.commons;

    // Open package for reflection (Spring, JavaFX, Hibernate, etc.)
    opens PlanetMel;

    exports PlanetMel;
    exports PlanetMel.domain;
    opens PlanetMel.domain;
    exports PlanetMel.repo;
    opens PlanetMel.repo;
    exports PlanetMel.service;
    opens PlanetMel.service;
    exports PlanetMel.controller;
    opens PlanetMel.controller;
}

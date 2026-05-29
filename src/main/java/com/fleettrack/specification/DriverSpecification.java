package com.fleettrack.specification;

import com.fleettrack.entity.Driver;
import org.springframework.data.jpa.domain.Specification;

public class DriverSpecification {

    public static Specification<Driver> hasLastName(
            String lastName) {
        return (root, query, cb) ->
                lastName == null ? null :
                cb.like(cb.lower(root.get("lastName")),
                        "%" + lastName.toLowerCase() + "%");
    }

    public static Specification<Driver> hasVehicleAssigned() {
        return (root, query, cb) ->
                cb.isNotNull(root.get("vehicle"));
    }

    public static Specification<Driver> hasNoVehicle() {
        return (root, query, cb) ->
                cb.isNull(root.get("vehicle"));
    }
}
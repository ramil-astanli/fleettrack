package com.fleettrack.specification;

import com.fleettrack.entity.Vehicle;
import com.fleettrack.enums.VehicleStatus;
import org.springframework.data.jpa.domain.Specification;

public class VehicleSpecification {

    public static Specification<Vehicle> hasStatus(
            VehicleStatus status) {
        return (root, query, cb) ->
                status == null ? null :
                        cb.equal(root.get("status"), status);
    }

    public static Specification<Vehicle> yearBetween(
            Integer from, Integer to) {
        return (root, query, cb) -> {
            if (from == null && to == null) return null;
            if (from == null) return cb.lessThanOrEqualTo(
                    root.get("year"), to);
            if (to == null) return cb.greaterThanOrEqualTo(
                    root.get("year"), from);
            return cb.between(root.get("year"), from, to);
        };
    }

    public static Specification<Vehicle> hasMake(String make) {
        return (root, query, cb) ->
                make == null ? null :
                        cb.like(cb.lower(root.get("make")),
                                "%" + make.toLowerCase() + "%");
    }

    public static Specification<Vehicle> hasLicensePlate(
            String licensePlate) {
        return (root, query, cb) ->
                licensePlate == null ? null :
                        cb.like(cb.lower(root.get("licensePlate")),
                                "%" + licensePlate.toLowerCase() + "%");
    }

}

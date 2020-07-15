package io.quarkus.it.mongodb.pojo;

import java.util.Optional;

import org.bson.types.ObjectId;

import io.quarkus.runtime.annotations.RegisterForReflection;

// we need to register String for reflection otherwise it will not be as resteasy didn't
// see https://github.com/quarkusio/quarkus/issues/10873
@RegisterForReflection(targets = String.class)
public class Pojo {
    public ObjectId id;
    public String description;
    public Optional<String> optionalString;
}

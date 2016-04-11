package com.fbo.services.core.model;

import com.fbo.services.core.resource.ParameterResource;
import com.fbo.services.core.resource.Resourceable;

/**
 * Created by Fred on 27/06/2015.
 */
public class GenericParameter<T> implements Resourceable<ParameterResource<T>> {


    public static final String PARAMETER_PREFIX_NAME = "*";

    private String name;
    private Bounds<T> bounds;
    private T[] possibleValues;
    private ConstraintKind constraintKind;
    private T objectValue;

    public GenericParameter() {
    }

    public T getObjectValue() {
        return objectValue;
    }

    private void setObjectValue(Object value) {
        this.objectValue = (T) value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bounds<T> getBounds() {
        return bounds;
    }

    public void setBounds(Bounds<T> bounds) {
        this.bounds = bounds;
    }

    public T[] getPossibleValues() {
        return possibleValues;
    }

    public void setPossibleValues(T[] possibleValues) {
        this.possibleValues = possibleValues;
    }

    public ConstraintKind getConstraintKind() {
        return constraintKind;
    }

    public void setConstraintKind(ConstraintKind constraintKind) {
        this.constraintKind = constraintKind;
    }

    @Override
    public final ParameterResource<T> toResource() {
        return new ParameterResource(this);
    }
}

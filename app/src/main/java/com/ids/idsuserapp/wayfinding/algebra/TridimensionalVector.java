package com.ids.idsuserapp.wayfinding.algebra;


import com.ids.idsuserapp.wayfinding.Checkpoint;

public class TridimensionalVector {
    public final double x;
    public final double y;
    public final double z;

    public TridimensionalVector(Checkpoint begin, Checkpoint end) {
        x = end.getX() - begin.getX();
        y = end.getY() - begin.getY();
        z = 0;
    }

    /**
     * Costruzione del vettore dalle coordinate
     *
     * @param x
     * @param y
     * @param z
     */
    public TridimensionalVector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getPlaneAngle(TridimensionalVector vector) {
        double cosine, planeAngle;
        TridimensionalVector crossProduct = vectorProduct(vector);
        cosine = cosine(vector);

        planeAngle = Math.acos(cosine);
        if (crossProduct.getZ() >= 0) {
            planeAngle = (2 * Math.PI) - planeAngle;
        }

        return planeAngle;
    }

    public double sine(TridimensionalVector vector) {
        TridimensionalVector vectorProduct = vectorProduct(vector);
        return vectorProduct.module() / (module() * vector.module());
    }

    public double cosine(TridimensionalVector vector) {
        return dotProduct(vector) / (module() * vector.module());
    }

    /**
     * Prodotto vettoriale
     *
     * @param otherVector
     * @return
     */
    public TridimensionalVector vectorProduct(TridimensionalVector otherVector) {
        return new TridimensionalVector(
                (this.y * otherVector.z) - (otherVector.y * this.z),
                (this.x * otherVector.z) - (otherVector.x * this.z),
                (this.x * otherVector.y) - (otherVector.x * this.y)
        );
    }

    /**
     * Modulo
     *
     * @return
     */
    public double module() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z, 2));
    }

    /**
     * Prodotto scalare
     *
     * @param otherVector
     * @return
     */
    public double dotProduct(TridimensionalVector otherVector) {
        return (x * otherVector.x) + (y * otherVector.y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "TridimensionalVector{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}

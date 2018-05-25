package com.itmo.dynamicgeoeventsanalyzer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

@Data
@NoArgsConstructor
public class Node<T> {
    private Square square;
    private Node<T> minmin;
    private Node<T> maxmax;
    private Node<T> minmax;
    private Node<T> maxmin;
    private Collection<DataAtPoint<T>> data = new CopyOnWriteArrayList<>();

    @Data
    @AllArgsConstructor
    public static class DataAtPoint<V> {
        private V data;
        private LatLong point;
    }

    public Node(final Square square) {
        this.square = square;
    }

    public Node(final Square square, final Collection<DataAtPoint<T>> data) {
        this.square = square;
        this.data = data;
    }

    @Value
    public static class Square {
        private Double minLat;
        private Double maxLat;
        private Double minLon;
        private Double maxLon;

        public Double getLength() {
            return abs(maxLat - minLat);
        }

        public Double getWidth() {
            return abs(maxLon - minLon);
        }

        public boolean contains(final Square square) {
            return square.minLat >= this.minLat &&
                    square.maxLat <= this.maxLat &&
                    square.minLon >= this.minLon &&
                    square.maxLon <= this.maxLon;
        }

        public boolean contains(final LatLong point) {
            return point.getLatitude() < getMaxLat() && point.getLatitude() > getMinLat() &&
                    point.getLongitude() < getMaxLon() && point.getLongitude() > getMinLon();
        }

        public LatLong getCenter() {
            return new LatLong(maxLat - (maxLat - minLat)/2, maxLon - (maxLon - minLon)/2);
        }
    }

    public void divide() {
        final Square minMinSquare = buildMinMinSquare();
        minmin = new Node<>(minMinSquare, getDataForSquare(minMinSquare));
        final Square minMaxSquare = buildMinMaxSquare();
        minmax = new Node<>(minMaxSquare, getDataForSquare(minMinSquare));
        final Square maxMaxSquare = buildMaxMaxSquare();
        maxmax = new Node<>(maxMaxSquare, getDataForSquare(minMinSquare));
        final Square maxMinSquare = buildMaxMinSquare();
        maxmin = new Node<>(maxMinSquare, getDataForSquare(minMinSquare));
    }

    private Collection<DataAtPoint<T>> getDataForSquare(Square square) {
        return this.data.stream().filter(e -> square.contains(e.point)).collect(Collectors.toList());
    }

    private Square buildMinMinSquare() {
        return new Square(this.square.minLat, this.square.maxLat - this.square.getLength() / 2, this.square.minLon, this.square.maxLon - this.square.getWidth() / 2);
    }

    private Square buildMinMaxSquare() {
        return new Square(this.square.minLat, this.square.maxLat - this.square.getLength() / 2, this.square.maxLon - this.square.getWidth() / 2, this.square.maxLon);
    }

    private Square buildMaxMaxSquare() {
        return new Square(this.square.maxLat - this.square.getLength() / 2, this.square.maxLat, this.square.maxLon - this.square.getWidth() / 2, this.square.maxLon);
    }

    private Square buildMaxMinSquare() {
        return new Square(this.square.maxLat - this.square.getLength() / 2, this.square.maxLat, this.square.minLon, this.square.maxLon - this.square.getWidth() / 2);
    }
}

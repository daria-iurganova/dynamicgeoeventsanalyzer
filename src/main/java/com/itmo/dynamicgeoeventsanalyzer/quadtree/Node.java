package com.itmo.dynamicgeoeventsanalyzer.quadtree;

import com.itmo.dynamicgeoeventsanalyzer.dto.LatLong;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.ArrayList;
import java.util.Collection;
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
    private Collection<DataAtPoint<T>> data = new ArrayList<>();

    @Data
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
        private Double minLong;
        private Double maxLong;

        public Double getLength() {
            return abs(maxLat - minLat);
        }

        public Double getWidth() {
            return abs(maxLong - minLong);
        }

        public boolean contains(final Square square) {
            return square.minLat >= this.minLat &&
                    square.maxLat <= this.maxLat &&
                    square.minLong >= this.minLong &&
                    square.maxLong <= this.maxLong;
        }

        public boolean contains(final LatLong point) {
            return point.getLatitude() < getMaxLat() && point.getLatitude() > getMinLat() &&
                    point.getLongitude() < getMaxLong() && point.getLongitude() > getMinLong();
        }
    }

    public void divide() {
        final Square minMinSquare = buildMinMinSquare();
        minmin = new Node<T>(minMinSquare, getDataForSquare(minMinSquare));
        final Square minMaxSquare = buildMinMaxSquare();
        minmax = new Node<T>(minMaxSquare, getDataForSquare(minMinSquare));
        final Square maxMaxSquare = buildMaxMaxSquare();
        maxmax = new Node<T>(maxMaxSquare, getDataForSquare(minMinSquare));
        final Square maxMinSquare = buildMaxMinSquare();
        maxmin = new Node<T>(maxMinSquare, getDataForSquare(minMinSquare));
    }

    private Collection<DataAtPoint<T>> getDataForSquare(Square square) {
        return this.data.stream().filter(e -> square.contains(e.point)).collect(Collectors.toList());
    }

    private Square buildMinMinSquare() {
        return new Square(this.square.minLat, this.square.maxLat - this.square.getLength() / 2, this.square.minLong, this.square.maxLong - this.square.getWidth() / 2);
    }

    private Square buildMinMaxSquare() {
        return new Square(this.square.minLat, this.square.maxLat - this.square.getLength() / 2, this.square.maxLong - this.square.getWidth() / 2, this.square.maxLong);
    }

    private Square buildMaxMaxSquare() {
        return new Square(this.square.maxLat - this.square.getLength() / 2, this.square.maxLat, this.square.maxLong - this.square.getWidth() / 2, this.square.maxLong);
    }

    private Square buildMaxMinSquare() {
        return new Square(this.square.maxLat - this.square.getLength() / 2, this.square.maxLat, this.square.minLong, this.square.maxLong - this.square.getWidth() / 2);
    }
}

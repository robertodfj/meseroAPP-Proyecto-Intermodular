package data.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

public class LineWithProduct {

    @Embedded
    public LineOrder lineOrder;

    @Relation(
            parentColumn = "productId",
            entityColumn = "id"
    )
    public Product product;
}
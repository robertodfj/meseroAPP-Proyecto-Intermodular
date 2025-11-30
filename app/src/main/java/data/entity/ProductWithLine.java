package data.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ProductWithLine {
    @Embedded
    public Product product;

    @Relation(
            parentColumn = "id",
            entityColumn = "productId"
    )
    public List<LineOrder> lines;
}

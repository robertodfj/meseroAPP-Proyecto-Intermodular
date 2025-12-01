package data.entity;

import androidx.room.Embedded;
import androidx.room.Relation;
import java.util.List;

public class OrderComplete {

    @Embedded
    public Order order;

    @Relation(
            entity = LineOrder.class,
            parentColumn = "id",
            entityColumn = "orderId"
    )
    public List<LineWithProduct> linesWithProducts;
}
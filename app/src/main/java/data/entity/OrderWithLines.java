package data.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class OrderWithLines {
    @Embedded
    public Order order;

    @Relation(
            parentColumn = "id",
            entityColumn = "orderId"
    )
    public List<LineOrder> lines;
}

package data.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class BarWithOrder {
    @Embedded
    public Bar bar;

    @Relation(
            parentColumn = "id",
            entityColumn = "barId"
    )
    public List<Order> orders;
}

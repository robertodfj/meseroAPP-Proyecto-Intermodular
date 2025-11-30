package data.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class BarWithTables {
    @Embedded
    public Bar bar;

    @Relation(
            parentColumn = "id",
            entityColumn = "barId"
    )
    public List<Table> tables;
}

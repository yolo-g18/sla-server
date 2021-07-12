package com.g18.dto;;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRoomResponse {
    private Long id;
    private String owner;
    private String name;
    private String description;
    private Instant createdDate;
    private Instant updateDate;
    private int numberOfMembers;
    private int numberOfStudySets;

}

package com.thoughtworks.rslist.entity;

import com.thoughtworks.rslist.dto.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Table(name = "event")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventEntity {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "name")
    private String eventName;
    private String keyWord;
    private Integer userId;
    @ColumnDefault("0")
    private Integer voteNum;

    public void setByEvent(Event event) {
        if (event.getEventName() != null) this.eventName = event.getEventName();
        if (event.getKeyWord() != null) this.keyWord = event.getKeyWord();
        if (event.getUserId() != null) this.userId = event.getUserId();
        if (event.getVoteNum() != null) this.voteNum = event.getVoteNum();
    }
}

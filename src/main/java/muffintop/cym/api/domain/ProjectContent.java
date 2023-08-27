package muffintop.cym.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PROJECT_CONTENT")
public class ProjectContent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    private String sessionId;

    private char type;

    private String content;

    private float positionX;

    private float positionY;

    private int positionZ;

    private float height;

    private float width;

    private String sender;

    private boolean isAnonymous;
    
    @JsonIgnore
    private char status;

    @CreationTimestamp
    @JsonIgnore
    private LocalDateTime createdDatetime;

    @UpdateTimestamp
    @JsonIgnore
    private LocalDateTime updatedDatetime;

}

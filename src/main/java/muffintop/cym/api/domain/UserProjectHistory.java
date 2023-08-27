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
import muffintop.cym.api.controller.enums.Status;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER_PROJECT_HISTORY")
public class UserProjectHistory implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private Status status;

    private LocalDateTime expiredDatetime;

    private boolean isOwner;

    @CreationTimestamp
    @JsonIgnore
    private LocalDateTime createdDatetime;

    @UpdateTimestamp
    @JsonIgnore
    private LocalDateTime updatedDatetime;

}

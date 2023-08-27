package muffintop.cym.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import muffintop.cym.api.controller.request.ProjectRequest;
import muffintop.cym.api.domain.enums.AuthMethod;
import muffintop.cym.api.domain.key.UserPk;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PROJECT")
public class Project implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String title;

    private String description;

    private String inviteCode;

    private int maxInviteNum;

    private String destination;

    private char type;

    @JsonIgnore
    private char status;

    @CreationTimestamp
    @JsonIgnore
    private LocalDateTime createdDatetime;

    @UpdateTimestamp
    @JsonIgnore
    private LocalDateTime updatedDatetime;

    @JsonIgnore
    private LocalDateTime expiredDatetime;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ProjectContent> projectContentList = new ArrayList<>();

    public void update(ProjectRequest request){
        title = request.getTitle();
        description = request.getTitle();
        inviteCode = request.getInviteCode();
        maxInviteNum = request.getMaxInviteNum();
        destination = request.getDestination();
        type = request.getType();
    }

}

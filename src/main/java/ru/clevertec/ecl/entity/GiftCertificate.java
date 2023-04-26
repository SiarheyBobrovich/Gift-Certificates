package ru.clevertec.ecl.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
@ToString(exclude = "tags")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(schema = "certificate", name = "gift_certificate")
public class GiftCertificate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 30)
    private String name;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "last_update_date", nullable = false)
    private LocalDateTime lastUpdateDate;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(schema = "certificate", name = "gc_tag",
            joinColumns = @JoinColumn(name = "gc_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "t_id", referencedColumnName = "id"))
    private List<Tag> tags;

    public void addTag(Tag tag) {
        if (Objects.isNull(tags)) {
            tags = new ArrayList<>();
        }
        tags.add(tag);
    }
}

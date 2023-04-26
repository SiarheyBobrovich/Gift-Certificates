package ru.clevertec.ecl.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@NamedQuery(name = "tagByName", query = "FROM Tag t WHERE t.name =:name")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
@Entity
@Table(schema = "certificate", name = "tag")
public class Tag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 20, unique = true, nullable = false)
    private String name;
}

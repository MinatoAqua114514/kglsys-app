package com.kglsys.domain.knowledgegraph;

import com.kglsys.domain.knowledgegraph.enums.ValueType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 知识图谱关系属性
 */
@Entity
@Table(name = "kg_relationship_properties")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KgRelationshipProperty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "relationship_id", nullable = false)
    private KgRelationship relationship;

    @Column(name = "property_name", nullable = false, length = 100)
    private String propertyName;

    @Lob
    @Column(name = "property_value", nullable = false)
    private String propertyValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "value_type", nullable = false)
    private ValueType valueType;
}
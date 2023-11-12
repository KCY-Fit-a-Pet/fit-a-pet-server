package com.kcy.fitapet.domain.pet.domain;

import com.kcy.fitapet.domain.care.domain.Care;
import com.kcy.fitapet.domain.member.domain.Manager;
import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.memo.domain.Memo;
import com.kcy.fitapet.domain.model.Auditable;
import com.kcy.fitapet.domain.pet.type.GenderType;
import com.kcy.fitapet.domain.pet.type.GenderTypeConverter;
import com.kcy.fitapet.domain.schedule.domain.Schedule;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PET")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"petName", "gender", "birth", "species"})
public class Pet extends Auditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "pet_name")
    private String petName;
    @Convert(converter = GenderTypeConverter.class)
    private GenderType gender;
    @Column(name = "pet_profile_img")
    private String petProfileImg;
    @ColumnDefault("false")
    private boolean neutered;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime birth;
    private Integer age;
    private String species;
    private String feed;

    @ManyToOne
    @JoinColumn(name = "master_id")
    private Member master;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private List<Manager> managers = new ArrayList<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private List<PetSchedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private List<PetCare> cares = new ArrayList<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private List<Memo> memos = new ArrayList<>();

    @Builder
    private Pet(String petName, GenderType gender, String petProfileImg, boolean neutered, LocalDateTime birth,
                Integer age, String species, String feed) {
        this.petName = petName;
        this.gender = gender;
        this.petProfileImg = petProfileImg;
        this.neutered = neutered;
        this.birth = birth;
        this.age = age;
        this.species = species;
        this.feed = feed;
    }

    public static Pet of(String petName, GenderType gender, String petProfileImg, boolean neutered, LocalDateTime birth,
                         Integer age, String species, String feed) {
        return Pet.builder()
                .petName(petName)
                .gender(gender)
                .petProfileImg(petProfileImg)
                .neutered(neutered)
                .birth(birth)
                .age(age)
                .species(species)
                .feed(feed).build();
    }
}

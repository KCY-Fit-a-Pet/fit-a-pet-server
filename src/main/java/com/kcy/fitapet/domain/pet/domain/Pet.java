package com.kcy.fitapet.domain.pet.domain;

import com.kcy.fitapet.domain.care.domain.Care;
import com.kcy.fitapet.domain.member.domain.Master;
import com.kcy.fitapet.domain.memo.domain.Memo;
import com.kcy.fitapet.domain.model.Auditable;
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
    private boolean gender;
    @Column(name = "pet_profile_img")
    private String petProfileImg;
    @ColumnDefault("false")
    private boolean neutered;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime birth;
    private Integer age;
    private String species;
    private String feed;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private List<Master> masters = new ArrayList<>();
    @ManyToMany
    @JoinTable(name = "PET_SCHEDULE",
            joinColumns = @JoinColumn(name = "pet_id"),
            inverseJoinColumns = @JoinColumn(name = "schedule_id"))
    private List<Schedule> schedules = new ArrayList<>();
    @ManyToMany
    @JoinTable(name = "PET_CARE",
            joinColumns = @JoinColumn(name = "pet_id"),
            inverseJoinColumns = @JoinColumn(name = "care_id"))
    private List<Care> cares = new ArrayList<>();
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private List<Memo> memos = new ArrayList<>();

    @Builder
    private Pet(String petName, boolean gender, String petProfileImg, boolean neutered, LocalDateTime birth,
                Integer age, String species, String feed, List<Master> masters, List<Schedule> schedules,
                List<Care> cares, List<Memo> memos) {
        this.petName = petName;
        this.gender = gender;
        this.petProfileImg = petProfileImg;
        this.neutered = neutered;
        this.birth = birth;
        this.age = age;
        this.species = species;
        this.feed = feed;
        this.masters = masters;
        this.schedules = schedules;
        this.cares = cares;
        this.memos = memos;
    }

    public static Pet of(String petName, boolean gender, String petProfileImg, boolean neutered, LocalDateTime birth,
                         Integer age, String species, String feed, List<Master> masters, List<Schedule> schedules,
                         List<Care> cares, List<Memo> memos) {
        return Pet.builder()
                .petName(petName)
                .gender(gender)
                .petProfileImg(petProfileImg)
                .neutered(neutered)
                .birth(birth)
                .age(age)
                .species(species)
                .feed(feed)
                .masters(masters)
                .schedules(schedules)
                .cares(cares)
                .memos(memos).build();
    }
}

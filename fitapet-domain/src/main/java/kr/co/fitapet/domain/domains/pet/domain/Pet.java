package kr.co.fitapet.domain.domains.pet.domain;

import jakarta.persistence.*;
import kr.co.fitapet.domain.common.model.DateAuditable;
import kr.co.fitapet.domain.domains.care.domain.CareCategory;
import kr.co.fitapet.domain.domains.manager.domain.Manager;
import kr.co.fitapet.domain.domains.memo.domain.MemoCategory;
import kr.co.fitapet.domain.domains.pet.type.GenderType;
import kr.co.fitapet.domain.common.converter.GenderTypeConverter;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PET")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"petName", "gender", "birthdate", "species"})
public class Pet extends DateAuditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "pet_name")
    private String petName;
    @Convert(converter = GenderTypeConverter.class)
    private GenderType gender;
    @Column(name = "pet_profile_img")
    private String petProfileImg;
    @ColumnDefault("false")
    private boolean neutered = false;
    @Temporal(TemporalType.DATE)
    private LocalDate birthdate;
    private String species;
    private String feed;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Manager> managers = new ArrayList<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PetSchedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CareCategory> careCategories = new ArrayList<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemoCategory> memoCategories = new ArrayList<>();

    @Builder
    private Pet(String petName, GenderType gender, String petProfileImg, boolean neutered, LocalDate birthdate,
                String species, String feed) {
        this.petName = petName;
        this.gender = gender;
        this.petProfileImg = petProfileImg;
        this.neutered = neutered;
        this.birthdate = birthdate;
        this.species = species;
        this.feed = feed;
    }

    public static Pet of(String petName, GenderType gender, String petProfileImg, boolean neutered, LocalDate birthdate,
                         String species, String feed) {
        return Pet.builder()
                .petName(petName)
                .gender(gender)
                .petProfileImg(petProfileImg)
                .neutered(neutered)
                .birthdate(birthdate)
                .species(species)
                .feed(feed).build();
    }
}

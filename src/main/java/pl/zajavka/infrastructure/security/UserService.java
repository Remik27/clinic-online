package pl.zajavka.infrastructure.security;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.zajavka.business.DoctorService;
import pl.zajavka.business.PatientService;
import pl.zajavka.domain.Doctor;
import pl.zajavka.domain.Patient;

import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public Integer getUserId(String username) {
    return userRepository.findByUserName(username).getId();
    }
    public void saveUser(UserDto userDto) {
        UserEntity user = UserEntity.builder()
                .userName(userDto.getName())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .active(true)
                .roles(getRoles(userDto))
                .build();
        UserEntity userEntity = userRepository.saveAndFlush(user);
        switch (userDto.getRole()) {
            case DOCTOR -> saveDoctor(userDto, userEntity.getId());
            case PATIENT -> savePatient(userDto, userEntity.getId());

        }
    }

    private void savePatient(UserDto userDto, Integer userId) {
        if (userDto.getName().isBlank() || userDto.getSurname().isBlank()){
            Patient patient = patientService.findPatientByPesel(userDto.getPesel());
            patientService.updatePatient(patient.withClinicUserId(userId));
        }
        Patient patient = Patient.builder()
                .name(userDto.getName())
                .surname(userDto.getSurname())
                .pesel(userDto.getPesel())
                .clinicUserId(userId)
                .build();
        patientService.addPatient(patient);
    }

    private void saveDoctor(UserDto userDto, Integer userId) {
        Doctor doctor = Doctor.builder()
                .name(userDto.getName())
                .surname(userDto.getSurname())
                .pesel(userDto.getPesel())
                .specialization(userDto.getSpecialization())
                .clinicUserId(userId)
                .build();
        doctorService.saveDoctor(doctor);
    }

    private Set<RoleEntity> getRoles(UserDto userDto) {
        return Set.of(roleRepository.findByRole(userDto.getRole().name()));
    }
}

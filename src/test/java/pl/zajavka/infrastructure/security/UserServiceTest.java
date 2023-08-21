package pl.zajavka.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.zajavka.business.DoctorService;
import pl.zajavka.business.PatientService;
import pl.zajavka.domain.Doctor;
import pl.zajavka.domain.Patient;
import pl.zajavka.util.DtoFixtures;
import pl.zajavka.util.EntityFixtures;

import java.util.Set;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    public static final String ENCODED_PASSWORD =  "encodedPassword";
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private DoctorService doctorService;

    @Mock
    private PatientService patientService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        Mockito.when(passwordEncoder.encode(anyString())).thenReturn(ENCODED_PASSWORD);
    }

    @Test
    public void saveUserAsDoctor() {
        // given
        Integer userId = 1;
        UserDto userDto = DtoFixtures.someUserDto()
                .withRole(Roles.DOCTOR);
        RoleEntity role = EntityFixtures.someRole();
        UserEntity userEntity = EntityFixtures.someUser();
        Doctor doctor = buildDoctor(userDto, userId);
        //when
        Mockito.when(roleRepository.findByRole(userDto.getRole().name())).thenReturn(role);
        Mockito.when(userRepository.saveAndFlush(userEntity.withPassword(ENCODED_PASSWORD))).thenReturn(userEntity.withId(userId));

        userService.saveUser(userDto);

        // then
        verify(doctorService, times(1)).saveDoctor(doctor);
    }

    private Doctor buildDoctor(UserDto userDto, Integer id) {
        return Doctor.builder()
                .name(userDto.getName())
                .surname(userDto.getSurname())
                .pesel(userDto.getPesel())
                .specialization(userDto.getSpecialization())
                .clinicUserId(id)
                .build();
    }

    @Test
    public void saveUserAsExistPatient() {
        // given
        Integer userId = 1;
        UserDto userDto = DtoFixtures.someUserDto().withName("").withSurname("").withRole(Roles.PATIENT);
        RoleEntity role = EntityFixtures.someRole().withRole(Roles.PATIENT.name());
        UserEntity userEntity = EntityFixtures.someUser().withRoles(Set.of(role));
        Patient patient = buildPatient(userDto, userId);

        //when
        Mockito.when(roleRepository.findByRole(userDto.getRole().name())).thenReturn(role);
        Mockito.when(userRepository.saveAndFlush(userEntity.withPassword(ENCODED_PASSWORD)))
                .thenReturn(userEntity.withId(userId));
        Mockito.when(patientService.findPatientByPesel(userDto.getPesel()))
                .thenReturn(patient.withClinicUserId(userId));

        userService.saveUser(userDto);

        // then
        verify(patientService, times(1)).updatePatient(patient.withClinicUserId(userId));
        verify(patientService, times(1)).findPatientByPesel(userDto.getPesel());
    }

    @Test
    public void saveUserAsNewPatient() {
        // given
        Integer userId = 1;
        UserDto userDto = DtoFixtures.someUserDto()
                .withRole(Roles.PATIENT);
        RoleEntity role = EntityFixtures.someRole().withRole(Roles.PATIENT.name());
        UserEntity userEntity = EntityFixtures.someUser().withRoles(Set.of(role));
        Patient patient = buildPatient(userDto, userId);
        //when
        Mockito.when(roleRepository.findByRole(userDto.getRole().name())).thenReturn(role);
        Mockito.when(userRepository.saveAndFlush(userEntity.withPassword(ENCODED_PASSWORD))).thenReturn(userEntity.withId(userId));

        userService.saveUser(userDto);

        // then
        verify(patientService, times(1)).addPatient(patient);
    }

    private Patient buildPatient(UserDto userDto, Integer userId) {
        return Patient.builder()
                .name(userDto.getName())
                .surname(userDto.getSurname())
                .pesel(userDto.getPesel())
                .clinicUserId(userId)
                .build();
    }


}


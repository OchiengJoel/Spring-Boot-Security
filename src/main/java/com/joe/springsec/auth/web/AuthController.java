package com.joe.springsec.auth.web;

import com.joe.springsec.auth.enums.ERole;
import com.joe.springsec.auth.jwt.JwtUtils;
import com.joe.springsec.auth.models.Role;
import com.joe.springsec.auth.models.User;
import com.joe.springsec.auth.payload.request.AssignCompaniesRequest;
import com.joe.springsec.auth.payload.request.LoginRequest;
import com.joe.springsec.auth.payload.request.SignupRequest;
import com.joe.springsec.auth.payload.response.ErrorResponse;
import com.joe.springsec.auth.payload.response.MessageResponse;
import com.joe.springsec.auth.payload.response.UserInfoResponse;
import com.joe.springsec.auth.repo.RoleRepository;
import com.joe.springsec.auth.repo.UserRepository;
import com.joe.springsec.auth.services.UserDetailsImpl;
import com.joe.springsec.company.model.Company;
import com.joe.springsec.company.repo.CompanyRepo;
import com.joe.springsec.email.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CompanyRepo companyRepo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        emailService.sendLoginNotificationEmail(userDetails);

        Long companyId = userDetails.getCompanyIds().isEmpty() ? null : userDetails.getCompanyIds().get(0);
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails, companyId);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        //List<String> permissions = userDetails.getPermissions(); // Get permissions from UserDetailsImpl

        List<Long> companyIds = userDetails.getCompanyIds();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserInfoResponse(userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles,
                        companyIds,
                        jwtCookie.getValue()));

//        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
//                .body(new UserInfoResponse(userDetails.getId(),
//                        userDetails.getUsername(),
//                        userDetails.getEmail(),
//                        roles,
//                        permissions, // Send permissions in response
//                        companyIds,
//                        jwtCookie.getValue()));
    }

//    @PostMapping("/signup")
//    @PreAuthorize("hasPermission(null, 'ADD_USER')")
//    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
//        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
//            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
//        }
//
//        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
//            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
//        }
//
//        User user = new User(signUpRequest.getUsername(),
//                signUpRequest.getEmail(),
//                encoder.encode(signUpRequest.getPassword()));
//
//        Set<String> strRoles = signUpRequest.getRole();
//        Set<Role> roles = new HashSet<>();
//
//        if (strRoles == null) {
//            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
//                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//            roles.add(userRole);
//        } else {
//            strRoles.forEach(role -> {
//                switch (role) {
//                    case "admin":
//                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roles.add(adminRole);
//                        break;
//                    case "mod":
//                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roles.add(modRole);
//                        break;
//                    default:
//                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roles.add(userRole);
//                }
//            });
//        }
//
//        user.setRoles(roles);
//        userRepository.save(user);
//        // Send the welcome email after the user has been saved, with company-specific email configuration
//        emailService.sendUserCreationEmail(user);
//        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
//    }

//    @PostMapping("/signup")
//    @PreAuthorize("hasPermission(null, 'ADD_USER')")
//    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
//
//        log.info("Received signup request for user: {}", signUpRequest.getUsername());
//        // Check if the username is already taken
//        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
//            return ResponseEntity.badRequest().body(new ErrorResponse("Error: Username is already taken!", HttpStatus.BAD_REQUEST.value()));
//        }
//
//        // Check if the email is already in use
//        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
//            return ResponseEntity.badRequest().body(new ErrorResponse("Error: Email is already in use!", HttpStatus.BAD_REQUEST.value()));
//        }
//
//        // Create new user object
//        User user = new User(signUpRequest.getUsername(),
//                signUpRequest.getEmail(),
//                encoder.encode(signUpRequest.getPassword()));
//
//        // Handle roles
//        Set<String> strRoles = signUpRequest.getRole();
//        Set<Role> roles = new HashSet<>();
//
//        if (strRoles == null || strRoles.isEmpty()) {
//            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
//                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//            roles.add(userRole);
//        } else {
//            strRoles.forEach(role -> {
//                Role userRole;
//                switch (role.toLowerCase()) {
//                    case "admin":
//                        userRole = roleRepository.findByName(ERole.ROLE_ADMIN)
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        break;
//                    case "mod":
//                        userRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        break;
//                    default:
//                        userRole = roleRepository.findByName(ERole.ROLE_USER)
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                }
//                roles.add(userRole);
//            });
//        }
//
//        user.setRoles(roles);
//        userRepository.save(user);
//
//        // Send welcome email
//        emailService.sendUserCreationEmail(user);
//        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
//    }


    @PostMapping("/signup")
    @PreAuthorize("hasRole('ADMIN') or hasPermission(null, 'ADD_USER')")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        log.info("Received signup request for user: {}", signUpRequest.getUsername());

        // Check if the username is already taken
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Error: Username is already taken!", HttpStatus.BAD_REQUEST.value()));
        }

        // Check if the email is already in use
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Error: Email is already in use!", HttpStatus.BAD_REQUEST.value()));
        }

        // Create new user object
        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));

        // Handle roles
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                Role userRole;
                switch (role.toLowerCase()) {
                    case "admin":
                        userRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        break;
                    case "mod":
                        userRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        break;
                    default:
                        userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                }
                roles.add(userRole);
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        emailService.sendUserCreationEmail(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }

    @PostMapping("/admin/assignCompanies")
    @PreAuthorize("hasPermission(null, 'ASSIGN_COMPANIES')")
    public ResponseEntity<?> assignCompanies(@Valid @RequestBody AssignCompaniesRequest request) {
        // Step 1: Find the user by ID
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        // Step 2: Check if any company IDs were provided
        if (request.getCompanyIds() == null || request.getCompanyIds().isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No company IDs provided."));
        }

        // Step 3: Fetch the companies by their IDs
        List<Company> companies = companyRepo.findAllById(request.getCompanyIds());

        // Step 4: Handle case where some company IDs do not exist in the database
        if (companies.size() != request.getCompanyIds().size()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: One or more company IDs are invalid."));
        }

        // Step 5: Create a new set to avoid modifying currentCompanies directly inside the lambda
        Set<Company> updatedCompanies = new HashSet<>(user.getCompanies());

        // Add only companies that are not already assigned
        companies.stream()
                .filter(company -> !updatedCompanies.contains(company))
                .forEach(updatedCompanies::add);

        // Step 6: Update the user's companies after processing the stream
        user.setCompanies(updatedCompanies);
        userRepository.save(user);

        // Step 7: Return success response
        return ResponseEntity.ok(new MessageResponse("Companies assigned successfully!"));
    }
}


//@PostMapping("/admin/createUser")
//public ResponseEntity<?> createUser(@Valid @RequestBody SignupRequest signUpRequest) {
//    // Check if username or email already exists
//    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
//        return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
//    }
//
//    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
//        return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
//    }
//
//    // Create new user's account
//    User user = new User(signUpRequest.getUsername(),
//            signUpRequest.getEmail(),
//            encoder.encode(signUpRequest.getPassword()));
//
//    // Assign roles to the new user
//    Set<Role> roles = new HashSet<>();
//    if (signUpRequest.getRole() != null) {
//        for (String role : signUpRequest.getRole()) {
//            Role userRole = roleRepository.findByName(ERole.valueOf(role.toUpperCase()))
//                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//            roles.add(userRole);
//        }
//    } else {
//        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
//                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//        roles.add(userRole);
//    }
//
//    user.setRoles(roles);
//    userRepository.save(user);
//    return ResponseEntity.ok(new MessageResponse("User created successfully!"));
//}
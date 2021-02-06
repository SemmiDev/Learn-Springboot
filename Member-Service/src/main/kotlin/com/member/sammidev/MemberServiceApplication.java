package com.member.sammidev;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@SpringBootApplication
@EnableMongoAuditing
public class MemberServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MemberServiceApplication.class, args);
    }
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class Authentication {
    private String id;
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "balances")
class Balance {
    @Id
    private String id;
    private String merchantId;
    private BigDecimal balance;
    private Long point;
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "members")
class Member {

    @Id
    private String id;
    private String merchantId;
    private String name;
    private String email;
    private String phone;
    private Boolean verified;
    @Version
    private Long version;
    @CreatedDate
    private Long createdAt;
    @LastModifiedDate
    private Long lastModifiedAt;
}

@Repository
interface MemberRepository extends MongoRepository<Member, String> {
    Member findByIdAndMerchantId(String id, String merchantId);
}

@Repository
interface BalanceRepository extends MongoRepository<Balance, String> {}

// exception
class AuthenticationException extends RuntimeException{}

@Component
class AuthenticationArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Authentication.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        String merchantId = webRequest.getHeader("X-MERCHANT-ID");
        if (merchantId == null) {
            throw new AuthenticationException();
        } else {
            return Authentication.builder()
                    .id(merchantId)
                    .build();
        }
    }
}

@Configuration
class Config implements WebMvcConfigurer {

    @Autowired
    private AuthenticationArgumentResolver authenticationArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticationArgumentResolver);
    }
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class CreateMemberServiceRequest {

    @NotBlank(message = "NotBlank")
    private String merchantId;

    @Email(message = "MustValid")
    @NotBlank(message = "NotBlank")
    private String email;

    @Pattern(regexp = "[0-9]+", message = "MustValid")
    @NotBlank(message = "NotBlank")
    private String phone;

    @NotBlank(message = "NotBlank")
    private String name;
}

interface MemberData {
    String getMemberId();
    String getMerchantId();
}

@Documented
@Constraint(validatedBy = {MemberMustExistsValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@interface MemberMustExists {
    String message();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

class MemberMustExistsValidator implements ConstraintValidator<MemberMustExists, MemberData> {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public boolean isValid(MemberData value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value.getMemberId()) || StringUtils.isEmpty(value.getMerchantId())) {
            return true;
        } else {
            return memberRepository.findByIdAndMerchantId(value.getMemberId(), value.getMerchantId()) != null;
        }
    }
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@MemberMustExists(message = "MustExists")
class GetMemberServiceRequest implements MemberData {

    @NotBlank(message = "NotBlank")
    private String memberId;

    @NotBlank(message = "NotBlank")
    private String merchantId;
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@MemberMustExists(message = "MustExists")
class UpdateMemberServiceRequest implements MemberData {

    @NotBlank(message = "NotBlank")
    private String memberId;

    @NotBlank(message = "NotBlank")
    private String merchantId;

    @NotBlank(message = "NotBlank")
    private String name;

    @NotNull(message = "NotNull")
    private Boolean verified;
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class CreateMemberWebRequest {
    private String email;
    private String phone;
    private String name;
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class UpdateMemberWebRequest {
    private String name;
    private Boolean verified;
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class CreateMemberWebResponse {
    private String id;
    private String email;
    private String phone;
    private String name;
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class GetMemberWebResponse {
    private String id;
    private String email;
    private String phone;
    private String name;
    private Balance balance;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Balance {
        private BigDecimal balance;
        private Long point;
    }

}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class UpdateMemberWebResponse {
    private String id;
    private String email;
    private String phone;
    private String name;
}

interface MemberService {
    CreateMemberWebResponse create(@Valid CreateMemberServiceRequest request);
    UpdateMemberWebResponse update(@Valid UpdateMemberServiceRequest request);
    GetMemberWebResponse get(@Valid GetMemberServiceRequest request);
}

@Service
@Validated
class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @Override
    public CreateMemberWebResponse create(@Valid CreateMemberServiceRequest request) {
        Member member = Member.builder()
                .id(UUID.randomUUID().toString())
                .email(request.getEmail())
                .phone(request.getPhone())
                .merchantId(request.getMerchantId())
                .name(request.getName())
                .verified(Boolean.FALSE)
                .build();

        member = memberRepository.save(member);

        Balance balance = Balance.builder()
                .id(member.getId())
                .merchantId(request.getMerchantId())
                .point(0L)
                .balance(BigDecimal.ZERO)
                .build();

        balance = balanceRepository.save(balance);

        return CreateMemberWebResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .phone(member.getPhone())
                .email(member.getEmail())
                .build();
    }

    @Override
    public UpdateMemberWebResponse update(@Valid UpdateMemberServiceRequest request) {
        Member member = memberRepository.findByIdAndMerchantId(request.getMemberId(), request.getMerchantId());
        member.setName(request.getName());
        member.setVerified(request.getVerified());

        memberRepository.save(member);

        return UpdateMemberWebResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .phone(member.getPhone())
                .email(member.getEmail())
                .build();
    }

    @Override
    public GetMemberWebResponse get(@Valid GetMemberServiceRequest request) {
        Member member = memberRepository.findByIdAndMerchantId(request.getMemberId(), request.getMerchantId());
        Balance balance = balanceRepository.findById(member.getId()).get();

        return GetMemberWebResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .phone(member.getPhone())
                .email(member.getEmail())
                .balance(GetMemberWebResponse.Balance.builder()
                        .balance(balance.getBalance())
                        .point(balance.getPoint())
                        .build())
                .build();
    }
}


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class WebResponse<T> {
    private Integer code;
    private String status;
    private T data;
}

@RestControllerAdvice
class ExceptionController {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public WebResponse<String> authenticationException(AuthenticationException e) {
        return WebResponse.<String>builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .status(HttpStatus.UNAUTHORIZED.name())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public WebResponse<String> constraintViolationException(ConstraintViolationException e) {
        return WebResponse.<String>builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST.name())
                .data(e.getMessage())
                .build();
    }
}

@RestController
@RequestMapping("/v1/members")
class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<CreateMemberWebResponse> create(Authentication authentication,
                                                       @RequestBody CreateMemberWebRequest request) {

        CreateMemberServiceRequest serviceRequest = CreateMemberServiceRequest.builder()
                .merchantId(authentication.getId())
                .build();
        BeanUtils.copyProperties(request, serviceRequest);

        CreateMemberWebResponse serviceResponse = memberService.create(serviceRequest);

        return WebResponse.<CreateMemberWebResponse>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.name())
                .data(serviceResponse)
                .build();
    }

    @PutMapping(value = "/{memberId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<UpdateMemberWebResponse> update(Authentication authentication,
                                                       @PathVariable("memberId") String memberId,
                                                       @RequestBody UpdateMemberWebRequest request) {

        UpdateMemberServiceRequest serviceRequest = UpdateMemberServiceRequest.builder()
                .memberId(memberId)
                .merchantId(authentication.getId())
                .build();
        BeanUtils.copyProperties(request, serviceRequest);

        UpdateMemberWebResponse serviceResponse = memberService.update(serviceRequest);

        return WebResponse.<UpdateMemberWebResponse>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.name())
                .data(serviceResponse)
                .build();
    }

    @GetMapping(value = "/{memberId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<GetMemberWebResponse> get(Authentication authentication,
                                                 @PathVariable("memberId") String memberId) {
        GetMemberServiceRequest serviceRequest = GetMemberServiceRequest.builder()
                .memberId(memberId)
                .merchantId(authentication.getId())
                .build();

        GetMemberWebResponse serviceResponse = memberService.get(serviceRequest);

        return WebResponse.<GetMemberWebResponse>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.name())
                .data(serviceResponse)
                .build();
    }
}
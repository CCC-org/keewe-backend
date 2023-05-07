package ccc.keeweapi.dto.user;

import ccc.keewedomain.persistence.domain.user.enums.VendorType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class AccountResponse {
    private VendorType vendorType;
    private String identifier;
}

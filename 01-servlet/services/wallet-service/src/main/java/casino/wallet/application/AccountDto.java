package casino.wallet.application;

/**
 * Data Transfer Object: what the service hands to the outside (the servlet).
 * The servlet never sees the domain Account, only this plain data holder.
 */
public record AccountDto(String username, long balance) {
}

<#import "template.ftl" as layout>

<@layout.registrationLayout displayMessage=false; section>
<#if section == "form">
<main class="codegap-info-shell">
    <section class="codegap-info-card" aria-labelledby="verify-title">
        <div class="login-brand">
            <span class="brand-mark" aria-hidden="true"><svg viewBox="0 0 32 32"><path d="M17.9 3.5 7.3 17.1h7.4l-.6 11.4 10.6-14.2h-7.3l.5-10.8Z"></path></svg></span>
            <span>Code<span class="brand-accent">Gap</span></span>
        </div>
        <div class="info-icon" aria-hidden="true">
            <svg viewBox="0 0 24 24"><path d="m5 12 4 4L19 6"></path><circle cx="12" cy="12" r="9"></circle></svg>
        </div>
        <span class="eyebrow">One last step</span>
        <h1 id="verify-title">Verify your email</h1>
        <p class="info-copy">We've sent a verification email to <strong>${user.email}</strong>. Please check your inbox and click the verification link.</p>
        <div class="info-actions">
            <form action="${url.loginAction}" method="post">
                <button class="info-primary-btn" type="submit">Resend email</button>
            </form>
            <p class="info-secondary">Wrong account? <a href="${url.loginUrl}">Back to login</a></p>
        </div>
    </section>
</main>
</#if>
</@layout.registrationLayout>

<#import "template.ftl" as layout>

<@layout.registrationLayout displayInfo=false; section>

<#if section == "form">
<main class="cg-login codegap-auth-shell">
    <section class="codegap-auth-panel">
        <div class="codegap-login-card">
            <div class="login-brand">
                <span class="brand-mark" aria-hidden="true">
                    <svg viewBox="0 0 32 32" role="img">
                        <path d="M17.9 3.5 7.3 17.1h7.4l-.6 11.4 10.6-14.2h-7.3l.5-10.8Z"></path>
                    </svg>
                </span>
                <span>Code<span class="brand-accent">Gap</span></span>
            </div>

            <div class="login-heading">
                <span class="eyebrow">Welcome back</span>
                <h1>Sign in to your coding workspace</h1>
                <p>Pick up where you left off and keep closing the gap.</p>
            </div>

            <#if message?has_content>
                <div class="login-alert login-alert-${message.type}" role="alert">
                    <span class="alert-icon" aria-hidden="true">
                        <#if message.type == "error">!</#if>
                        <#if message.type == "success">✓</#if>
                        <#if message.type != "error" && message.type != "success">i</#if>
                    </span>
                    <span>${kcSanitize(message.summary)?no_esc}</span>
                </div>
            </#if>

            <form id="kc-form-login" action="${url.loginAction}" method="post" class="login-form">
                <div class="form-field">
                    <label for="username">Email or username</label>
                    <div class="input-wrap">
                        <svg class="input-icon" viewBox="0 0 24 24" aria-hidden="true">
                            <path d="M20 21a8 8 0 0 0-16 0M12 13a4 4 0 1 0 0-8 4 4 0 0 0 0 8Z"></path>
                        </svg>
                        <input type="text" id="username" name="username"
                               placeholder="you@example.com"
                               value="${(login.username!'')}"
                               autofocus autocomplete="username" aria-label="Email or username"/>
                    </div>
                </div>

                <div class="form-field">
                    <div class="field-label-row">
                        <label for="password">Password</label>
                        <#if realm.resetPasswordAllowed>
                            <a class="quiet-link" href="${url.loginResetCredentialsUrl}">Forgot password?</a>
                        </#if>
                    </div>
                    <div class="input-wrap password-field">
                        <svg class="input-icon" viewBox="0 0 24 24" aria-hidden="true">
                            <path d="M7 10V7a5 5 0 0 1 10 0v3M5 10h14v11H5V10Z"></path>
                        </svg>
                        <input type="password" id="password" name="password"
                               placeholder="Enter your password"
                               autocomplete="current-password" aria-label="Password"/>
                        <button class="toggle-password" type="button" aria-label="Show password"
                                onclick="togglePassword(this)">
                            <svg viewBox="0 0 24 24" aria-hidden="true">
                                <path d="M2 12s3.5-6 10-6 10 6 10 6-3.5 6-10 6S2 12 2 12Z"></path>
                                <circle cx="12" cy="12" r="2.5"></circle>
                            </svg>
                        </button>
                    </div>
                </div>

                <button id="kc-login" class="login-btn" type="submit">
                    <span class="login-btn-label">Sign in</span>
                    <span class="login-spinner" aria-hidden="true"></span>
                </button>
            </form>

            <#if social.providers?? && social.providers?has_content>
                <div class="login-divider"><span>or continue with</span></div>
                <div class="social-login">
                    <#list social.providers as provider>
                        <a class="social-btn" href="${provider.loginUrl}" aria-label="Continue with ${provider.displayName}">
                            <#if provider.alias == "google">
                                <svg class="google-icon" viewBox="0 0 24 24" aria-hidden="true">
                                    <path fill="#4285F4" d="M21.35 12.21c0-.72-.06-1.42-.18-2.09H12v3.96h5.24a4.49 4.49 0 0 1-1.94 2.95v2.45h3.14c1.84-1.7 2.91-4.2 2.91-7.27Z"></path>
                                    <path fill="#34A853" d="M12 21.67c2.63 0 4.84-.87 6.45-2.36l-3.14-2.45c-.87.58-1.98.92-3.31.92-2.54 0-4.69-1.72-5.46-4.03H3.3v2.53A9.74 9.74 0 0 0 12 21.67Z"></path>
                                    <path fill="#FBBC05" d="M6.54 13.75A5.85 5.85 0 0 1 6.24 12c0-.61.1-1.2.3-1.75V7.72H3.3A9.75 9.75 0 0 0 2.25 12c0 1.57.38 3.05 1.05 4.28l3.24-2.53Z"></path>
                                    <path fill="#EA4335" d="M12 6.22c1.43 0 2.71.49 3.72 1.46l2.79-2.79C16.84 3.3 14.63 2.33 12 2.33a9.74 9.74 0 0 0-8.7 5.39l3.24 2.53c.77-2.31 2.92-4.03 5.46-4.03Z"></path>
                                </svg>
                            <#else>
                                <span class="social-provider-mark" aria-hidden="true">${provider.displayName?substring(0, 1)}</span>
                            </#if>
                            <span>Continue with ${provider.displayName}</span>
                        </a>
                    </#list>
                </div>
            </#if>

            <#if realm.registrationAllowed>
                <p class="registration-prompt">
                    New here? <a class="quiet-link" href="${url.registrationUrl}">Create an account</a>
                </p>
            </#if>
        </div>
    </section>

    <aside class="codegap-hero" aria-label="CodeGap overview">
        <div class="hero-orb hero-orb-green"></div>
        <div class="hero-orb hero-orb-indigo"></div>
        <div class="hero-content">
            <span class="eyebrow">Your competitive programming cockpit</span>
            <h2>Close the gap between practice and progress.</h2>
            <p>Connect your coding profiles and get one clear view of your strengths, weak spots, and next best problems.</p>
            <div class="hero-stats" aria-label="CodeGap highlights">
                <span><strong>500+</strong> problems tracked</span>
                <span><strong>3</strong> platforms connected</span>
                <span><strong>1</strong> clearer path forward</span>
            </div>
        </div>
        <div class="code-snippet" aria-hidden="true">
            <span class="code-keyword">const</span> progress = <span class="code-value">consistent</span>;
        </div>
    </aside>
</main>
</#if>
</@layout.registrationLayout>

<script>
function togglePassword(button) {
    const input = document.getElementById("password");
    const visible = input.type === "password";
    input.type = visible ? "text" : "password";
    button.setAttribute("aria-label", visible ? "Hide password" : "Show password");
}

const loginForm = document.getElementById("kc-form-login");
if (loginForm) {
    loginForm.addEventListener("submit", function () {
        const button = document.getElementById("kc-login");
        button.classList.add("is-loading");
        button.disabled = true;
    });
}
</script>

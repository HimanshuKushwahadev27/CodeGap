<!DOCTYPE html>
<html>
<head>

<title>Email Verification</title>

<link rel="stylesheet" href="${url.resourcesPath}/css/styles.css">

</head>

<body>

<div class="verify-container">

    <div class="verify-card">

        <div class="login-brand">
            <span class="brand-mark" aria-hidden="true">
                <svg viewBox="0 0 32 32" role="img">
                    <path d="M17.9 3.5 7.3 17.1h7.4l-.6 11.4 10.6-14.2h-7.3l.5-10.8Z"></path>
                </svg>
            </span>
            <span>Code<span class="brand-accent">Gap</span></span>
        </div>

        <span class="eyebrow">One last step</span>
        <h2>Verify your email</h2>

        <p>
            We've sent a verification email to
            <strong>${user.email}</strong>
        </p>

        <p>Please check your inbox and click the verification link.</p>

        <form action="${url.loginAction}" method="post">
            <button class="primary-btn">
                Resend Email
            </button>
        </form>

    </div>

</div>

</body>
</html>
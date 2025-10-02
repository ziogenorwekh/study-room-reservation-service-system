/**
 * 
 */
async function findusername(name, email) {
    const response = await fetch('/api/find', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ name: name.value, email: email.value })
    });

    const resultDiv = document.getElementById('usernameResult');
    resultDiv.innerHTML = ''; // 초기화

    if (response.ok) {
        const data = await response.json();
        const usernames = data.username;

        if (usernames.length === 0) {
            resultDiv.innerHTML = `<div class="alert alert-warning mt-3">일치하는 아이디가 없습니다.</div>`;
            return;
        }

        const list = document.createElement('ul');
        list.classList.add('list-group', 'mt-3');

        usernames.forEach(username => {
            const item = document.createElement('li');
            item.classList.add('list-group-item');
            item.textContent = `ID: ${username}`;
            list.appendChild(item);
        });

        resultDiv.appendChild(list);
    } else {
        resultDiv.innerHTML = `<div class="alert alert-danger mt-3">서버 오류 또는 일치하는 회원 정보가 없습니다.</div>`;
    }
}

let isUsernameChecked = false;
const password = document.getElementById('password');
const passwordConfirm = document.getElementById('password-confirm');
const passwordInput = document.getElementById('password-group').querySelector('input');
const passwordConfirmInput = document.getElementById('password-confirm-group').querySelector('input');
const passwordCheckDiv = document.getElementById('password-check');
const passwordConfirmCheckDiv = document.getElementById('password-confirm-check');
async function checkUsername() {
    const username = document.querySelector('input[name="username"]').value;
    const resultDiv = document.getElementById('result');
    if (!username) {
        resultDiv.textContent = '아이디를 입력하세요.';
        resultDiv.style.color = 'red';
        return;
    } else if (username.length < 3) {
        resultDiv.textContent = '아이디는 최소 4자 이상이어야 합니다.';
        resultDiv.style.color = 'red';
        return;
    }
    try {
        const response = await fetch(`/api/check-username?username=${username}`);
        const data = await response.json();

        if (data.available) {
            resultDiv.textContent = '사용 가능한 아이디입니다.';
            resultDiv.style.color = 'green';
            isUsernameChecked = true;
        } else {
            resultDiv.textContent = '이미 사용 중인 아이디입니다.';
            resultDiv.style.color = 'red';
        }
    } catch (error) {
        console.error('Error checking username:', error);
        resultDiv.textContent = '오류가 발생했습니다. 다시 시도해주세요.';
        resultDiv.style.color = 'red';
    }
    
}
document.querySelector('.auth-form').addEventListener('submit', e => {
    if (!isUsernameChecked) {
        e.preventDefault(); // 폼 제출 막기
        alert('아이디 중복 확인을 먼저 해주세요.');
    }
    if (passwordInput.value.length < 8) {
        e.preventDefault(); // 폼 제출 막기
        alert('비밀번호는 최소 8자 이상이어야 합니다.');
        return;
    } else if (passwordInput.value !== passwordConfirmInput.value) {
        e.preventDefault(); // 폼 제출 막기
        alert('비밀번호와 비밀번호 확인이 일치하지 않습니다.');
        return;
    }
});

passwordInput.addEventListener('input', () => {
    if(passwordInput.value.length < 8) {
        
        passwordCheckDiv.textContent = '비밀번호는 최소 8자 이상이어야 합니다.';
        passwordCheckDiv.style.color = 'red';
        passwordInput.setCustomValidity('비밀번호는 최소 8자 이상이어야 합니다.');
        password.classList.add('is-invalid');
    } else {
        passwordCheckDiv.textContent = '';
        passwordInput.setCustomValidity('');
        password.classList.remove('is-invalid');
    }
});
passwordConfirmInput.addEventListener('input', () => {
    if(passwordInput.value !== passwordConfirmInput.value) {
        passwordConfirmCheckDiv.textContent = '비밀번호가 일치하지 않습니다.';
        passwordConfirmCheckDiv.style.color = 'red';
        passwordConfirmInput.setCustomValidity('비밀번호가 일치하지 않습니다.');
        passwordConfirm.classList.add('is-invalid');
    } else {
        passwordConfirmCheckDiv.textContent = '';
        passwordConfirmInput.setCustomValidity('');
        passwordConfirm.classList.remove('is-invalid');
    }
});

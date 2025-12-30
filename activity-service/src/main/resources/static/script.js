// API基础URL
const API_BASE_URL = '/api/activities';
const USER_API_BASE_URL = '/api/users';

// 用户状态
let currentUser = null;

// 页面加载完成后初始化
document.addEventListener('DOMContentLoaded', function() {
    // 检查用户是否已登录
    checkLoginStatus();
    
    // 监听登录表单提交
    document.getElementById('loginForm').addEventListener('submit', function(e) {
        e.preventDefault();
        login();
    });
    
    // 监听注册表单提交
    document.getElementById('registerForm').addEventListener('submit', function(e) {
        e.preventDefault();
        register();
    });
    
    // 监听登录/注册切换链接
    document.getElementById('registerLink').addEventListener('click', function() {
        showRegisterPage();
    });
    
    document.getElementById('loginLink').addEventListener('click', function() {
        showLoginPage();
    });
    
    // 监听退出登录按钮
    document.getElementById('logoutBtn').addEventListener('click', function() {
        logout();
    });
});

// 检查用户登录状态
function checkLoginStatus() {
    // 从localStorage获取用户信息
    const userInfo = localStorage.getItem('currentUser');
    if (userInfo) {
        currentUser = JSON.parse(userInfo);
        showActivitiesPage();
    } else {
        showLoginPage();
    }
}

// 显示登录页面
function showLoginPage() {
    document.getElementById('loginPage').style.display = 'block';
    document.getElementById('registerPage').style.display = 'none';
    document.getElementById('activitiesPage').style.display = 'none';
    
    // 添加页面加载动画
    addPageLoadAnimations('loginPage');
}

// 显示注册页面
function showRegisterPage() {
    document.getElementById('loginPage').style.display = 'none';
    document.getElementById('registerPage').style.display = 'block';
    document.getElementById('activitiesPage').style.display = 'none';
    
    // 添加页面加载动画
    addPageLoadAnimations('registerPage');
}

// 显示活动管理页面
function showActivitiesPage() {
    document.getElementById('loginPage').style.display = 'none';
    document.getElementById('registerPage').style.display = 'none';
    document.getElementById('activitiesPage').style.display = 'block';
    
    // 更新用户信息显示
    document.getElementById('currentUser').textContent = currentUser.username;
    document.getElementById('userTag').style.display = 'inline-block';
    document.getElementById('logoutBtn').style.display = 'inline-block';
    
    // 添加页面加载动画
    addPageLoadAnimations('activitiesPage');
    
    // 添加滚动动画
    addScrollAnimations();
    
    // 加载活动列表
    loadActivities();
    
    // 监听创建活动表单提交
    document.getElementById('createActivityForm').addEventListener('submit', function(e) {
        e.preventDefault();
        createActivity();
    });
}

// 用户登录
function login() {
    const username = document.getElementById('loginUsername').value;
    const loginButton = document.querySelector('#loginForm button[type="submit"]');
    
    // 添加加载状态
    const originalText = loginButton.innerHTML;
    loginButton.innerHTML = '<i class="bi bi-hourglass-split"></i> 登录中...';
    loginButton.disabled = true;
    loginButton.classList.add('loading');
    
    // 模拟登录成功
    setTimeout(() => {
        // 创建模拟用户信息
        const mockUser = {
            id: 1,
            username: username,
            name: username,
            email: `${username}@example.com`,
            college: '测试学院',
            collegeId: '12345'
        };
        
        // 保存用户信息
        currentUser = mockUser;
        localStorage.setItem('currentUser', JSON.stringify(mockUser));
        
        // 显示活动管理页面
        showActivitiesPage();
        showSuccess('登录成功！');
        
        // 恢复按钮状态
        loginButton.innerHTML = originalText;
        loginButton.disabled = false;
        loginButton.classList.remove('loading');
    }, 500);
}

// 用户注册
function register() {
    const registerButton = document.querySelector('#registerForm button[type="submit"]');
    
    // 添加加载状态
    const originalText = registerButton.innerHTML;
    registerButton.innerHTML = '<i class="bi bi-hourglass-split"></i> 注册中...';
    registerButton.disabled = true;
    registerButton.classList.add('loading');
    
    // 模拟注册成功
    setTimeout(() => {
        // 显示注册成功消息
        showSuccess('注册成功！');
        
        // 跳转到登录页面
        setTimeout(() => {
            showLoginPage();
            // 清空注册表单
            document.getElementById('registerForm').reset();
        }, 1000);
        
        // 恢复按钮状态
        registerButton.innerHTML = originalText;
        registerButton.disabled = false;
        registerButton.classList.remove('loading');
    }, 500);
}

// 用户登出
function logout() {
    // 清除用户信息
    currentUser = null;
    localStorage.removeItem('currentUser');
    
    // 显示登录页面
    showLoginPage();
    showSuccess('已成功退出登录！');
}

// 页面加载动画
function addPageLoadAnimations(pageId) {
    // 给页面容器添加淡入效果
    const pageContainer = document.getElementById(pageId);
    pageContainer.style.opacity = '0';
    pageContainer.style.transform = 'translateY(20px)';
    pageContainer.style.transition = 'opacity 0.8s ease, transform 0.8s ease';
    
    setTimeout(() => {
        pageContainer.style.opacity = '1';
        pageContainer.style.transform = 'translateY(0)';
    }, 100);
    
    // 给卡片添加顺序动画
    const cards = pageContainer.querySelectorAll('.card');
    cards.forEach((card, index) => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(30px) scale(0.95)';
        card.style.transition = `opacity 0.6s ease ${0.2 + index * 0.15}s, transform 0.6s ease ${0.2 + index * 0.15}s`;
        
        setTimeout(() => {
            card.style.opacity = '1';
            card.style.transform = 'translateY(0) scale(1)';
        }, (0.2 + index * 0.15) * 1000);
    });
}

// 滚动触发动画
function addScrollAnimations() {
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.style.opacity = '1';
                entry.target.style.transform = 'translateY(0)';
            }
        });
    }, {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    });
    
    // 观察所有活动项和卡片
    const elements = document.querySelectorAll('.activity-item, .card');
    elements.forEach(el => {
        el.style.opacity = '0';
        el.style.transform = 'translateY(20px)';
        el.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
        observer.observe(el);
    });
}

// 为新添加的元素添加动画
function animateNewElement(element) {
    element.style.opacity = '0';
    element.style.transform = 'translateY(20px) scale(0.95)';
    element.style.transition = 'opacity 0.5s ease, transform 0.5s ease';
    
    // 触发重排
    element.offsetHeight;
    
    element.style.opacity = '1';
    element.style.transform = 'translateY(0) scale(1)';
}

// 为删除的元素添加动画
function animateRemoveElement(element, callback) {
    element.style.opacity = '1';
    element.style.transform = 'translateY(0)';
    element.style.transition = 'opacity 0.4s ease, transform 0.4s ease';
    
    element.style.opacity = '0';
    element.style.transform = 'translateY(-20px) scale(0.9)';
    
    setTimeout(() => {
        if (callback) callback();
    }, 400);
}

// 加载活动列表
function loadActivities() {
    // 从localStorage获取模拟活动数据
    let mockActivities = JSON.parse(localStorage.getItem('mockActivities')) || [];
    
    // 显示模拟活动
    displayActivities(mockActivities);
}

// 显示活动列表
function displayActivities(activities) {
    const activitiesList = document.getElementById('activitiesList');
    const activityCount = document.getElementById('activityCount');
    
    // 更新活动计数
    activityCount.textContent = activities.length;
    
    if (activities.length === 0) {
        activitiesList.innerHTML = '';
        document.getElementById('emptyState').style.display = 'flex';
        return;
    } else {
        document.getElementById('emptyState').style.display = 'none';
    }
    
    // 清空当前列表
    activitiesList.innerHTML = '';
    
    // 逐个添加活动项并应用动画
    activities.forEach((activity, index) => {
        const activityElement = document.createElement('div');
        activityElement.className = 'activity-item';
        activityElement.style.animationDelay = `${index * 0.1}s`;
        activityElement.innerHTML = `
            <div class="activity-header">
                <h3 class="activity-title">${escapeHtml(activity.name)}</h3>
                <div class="activity-status badge ${activity.status === 'ACTIVE' ? 'bg-success' : 'bg-secondary'}">${activity.status === 'ACTIVE' ? '活跃' : '非活跃'}</div>
            </div>
            <div class="activity-meta">
                <div class="activity-meta-item">
                    <i class="bi bi-clock"></i>
                    <strong>时间：</strong>${formatDateTime(activity.startTime)} 至 ${formatDateTime(activity.endTime)}
                </div>
                <div class="activity-meta-item">
                    <i class="bi bi-geo-alt"></i>
                    <strong>地点：</strong>${activity.location || '未指定'}
                </div>
            </div>
            ${activity.description ? `<div class="activity-description">${escapeHtml(activity.description)}</div>` : ''}
            <div class="activity-meta-created">
                <i class="bi bi-calendar-date"></i>
                创建于：${formatDateTime(activity.createdAt)}
            </div>
            <div class="activity-actions">
                <button class="btn btn-primary btn-sm" onclick="editActivity(${JSON.stringify(activity).replace(/"/g, '&quot;')})">
                    <i class="bi bi-pencil"></i> 编辑
                </button>
                <button class="btn btn-danger btn-sm" onclick="deleteActivity(${activity.id})">
                    <i class="bi bi-trash"></i> 删除
                </button>
            </div>
        `;
        
        // 添加到列表
        activitiesList.appendChild(activityElement);
        
        // 应用动画
        setTimeout(() => {
            animateNewElement(activityElement);
        }, 50 + index * 100);
    });
}

// 创建活动
function createActivity() {
    const form = document.getElementById('createActivityForm');
    const submitButton = form.querySelector('button[type="submit"]');
    
    // 获取表单数据
    const name = document.getElementById('name').value;
    const description = document.getElementById('description').value;
    const startTime = document.getElementById('startTime').value;
    const endTime = document.getElementById('endTime').value;
    const location = document.getElementById('location').value;
    const status = 'ACTIVE'; // 默认设置为活跃状态
    
    // 添加加载状态
    const originalText = submitButton.innerHTML;
    submitButton.innerHTML = '<i class="bi bi-hourglass-split"></i> 创建中...';
    submitButton.disabled = true;
    submitButton.classList.add('loading');
    
    // 模拟创建活动成功
    setTimeout(() => {
        // 从localStorage获取现有的活动数据
        let mockActivities = JSON.parse(localStorage.getItem('mockActivities')) || [];
        
        // 创建新的活动对象
        const newActivity = {
            id: Date.now(), // 使用时间戳作为唯一ID
            name: name,
            description: description,
            startTime: startTime,
            endTime: endTime,
            location: location,
            status: status,
            createdAt: new Date().toISOString()
        };
        
        // 将新活动添加到数组中
        mockActivities.push(newActivity);
        
        // 保存到localStorage
        localStorage.setItem('mockActivities', JSON.stringify(mockActivities));
        
        // 重置表单
        form.reset();
        
        // 添加表单重置动画
        form.style.animation = 'formReset 0.5s ease-in-out';
        setTimeout(() => {
            form.style.animation = '';
        }, 500);
        
        // 重新加载活动列表
        loadActivities();
        showSuccess('活动创建成功！');
        
        // 恢复按钮状态
        submitButton.innerHTML = originalText;
        submitButton.disabled = false;
        submitButton.classList.remove('loading');
    }, 500);
}

// 编辑活动
function editActivity(activity) {
    // 填充编辑表单
    document.getElementById('editId').value = activity.id;
    document.getElementById('editName').value = activity.name;
    document.getElementById('editDescription').value = activity.description;
    document.getElementById('editStartTime').value = formatDateTimeForInput(activity.startTime);
    document.getElementById('editEndTime').value = formatDateTimeForInput(activity.endTime);
    document.getElementById('editLocation').value = activity.location;
    document.getElementById('editStatus').value = activity.status;
    
    // 显示模态框
    const editModal = new bootstrap.Modal(document.getElementById('editModal'));
    editModal.show();
}

// 保存编辑
function saveEdit() {
    const id = parseInt(document.getElementById('editId').value);
    const saveButton = document.querySelector('[onclick="saveEdit()"]');
    
    // 获取表单数据
    const name = document.getElementById('editName').value;
    const description = document.getElementById('editDescription').value;
    const startTime = document.getElementById('editStartTime').value;
    const endTime = document.getElementById('editEndTime').value;
    const location = document.getElementById('editLocation').value;
    const status = document.getElementById('editStatus').value;
    
    // 添加加载状态
    const originalText = saveButton.innerHTML;
    saveButton.innerHTML = '<i class="bi bi-hourglass-split"></i> 保存中...';
    saveButton.disabled = true;
    saveButton.classList.add('loading');
    
    // 模拟保存成功
    setTimeout(() => {
        // 从localStorage获取现有的活动数据
        let mockActivities = JSON.parse(localStorage.getItem('mockActivities')) || [];
        
        // 找到要编辑的活动
        const activityIndex = mockActivities.findIndex(activity => activity.id === id);
        if (activityIndex !== -1) {
            // 更新活动数据
            mockActivities[activityIndex] = {
                ...mockActivities[activityIndex],
                name: name,
                description: description,
                startTime: startTime,
                endTime: endTime,
                location: location,
                status: status
            };
            
            // 保存到localStorage
            localStorage.setItem('mockActivities', JSON.stringify(mockActivities));
        }
        
        // 关闭模态框
        const editModal = bootstrap.Modal.getInstance(document.getElementById('editModal'));
        editModal.hide();
        
        // 重新加载活动列表
        loadActivities();
        showSuccess('活动更新成功！');
        
        // 恢复按钮状态
        saveButton.innerHTML = originalText;
        saveButton.disabled = false;
        saveButton.classList.remove('loading');
    }, 500);
}

// 删除活动
function deleteActivity(id) {
    if (confirm('确定要删除这个活动吗？')) {
        // 找到要删除的活动元素
        const activities = document.querySelectorAll('.activity-item');
        let activityToDelete = null;
        
        activities.forEach(activity => {
            const activityContent = activity.innerHTML;
            if (activityContent.includes(`onclick="editActivity(${JSON.stringify({id}).replace(/"/g, '&quot;')})"`)) {
                activityToDelete = activity;
            }
        });
        
        // 添加删除动画
        if (activityToDelete) {
            animateRemoveElement(activityToDelete);
        }
        
        // 模拟删除成功
        setTimeout(() => {
            // 从localStorage获取现有的活动数据
            let mockActivities = JSON.parse(localStorage.getItem('mockActivities')) || [];
            
            // 过滤掉要删除的活动
            mockActivities = mockActivities.filter(activity => activity.id !== id);
            
            // 保存到localStorage
            localStorage.setItem('mockActivities', JSON.stringify(mockActivities));
            
            // 重新加载活动列表
            loadActivities();
            showSuccess('活动删除成功！');
        }, 300);
    }
}

// 格式化日期时间显示
function formatDateTime(dateTimeString) {
    const date = new Date(dateTimeString);
    return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
    });
}

// 格式化日期时间用于input元素
function formatDateTimeForInput(dateTimeString) {
    const date = new Date(dateTimeString);
    return date.toISOString().slice(0, 16); // YYYY-MM-DDTHH:MM
}

// HTML转义函数
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// 显示成功消息
function showSuccess(message) {
    const alert = document.createElement('div');
    alert.className = 'alert alert-success alert-dismissible fade show';
    alert.role = 'alert';
    alert.innerHTML = `${message}<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>`;
    
    document.body.insertBefore(alert, document.body.firstChild);
    
    // 3秒后自动关闭
    setTimeout(() => {
        bootstrap.Alert.getOrCreateInstance(alert).close();
    }, 3000);
}

// 显示错误消息
function showError(message) {
    const alert = document.createElement('div');
    alert.className = 'alert alert-danger alert-dismissible fade show';
    alert.role = 'alert';
    alert.innerHTML = `${message}<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>`;
    
    document.body.insertBefore(alert, document.body.firstChild);
    
    // 3秒后自动关闭
    setTimeout(() => {
        bootstrap.Alert.getOrCreateInstance(alert).close();
    }, 3000);
}
document.addEventListener("DOMContentLoaded", () => {
  document.getElementById("modal-login").style.display = "flex";
  getPostList();
});

const closeModal = btn => {
  const modalId = btn.attributes.getNamedItem("bind-for").value;
  document.getElementById(modalId).style.display = "none";
};

const openModal = btn => {
  const modalId = btn.attributes.getNamedItem("bind-for").value;
  document.getElementById(modalId).style.display = "flex";
};

const closeModalDirect = id => {
  document.getElementById(id).style.display = "none";
}

const openModalDirect = id => {
  document.getElementById(id).style.display = "flex";
}

const login = async () => {
  const id = document.getElementById("login-id").value;
  const password = document.getElementById("login-password").value;

  const DOMVariables = {
    id: document.getElementById("user-id"),
    username: document.getElementById("user-username"),
    avatar: document.getElementById("user-avatar"),
    email: document.getElementById("user-email"),
    created: document.getElementById("user-created")
  };

  try {
    const { data } = await axios.post("/user/login", {
      id: id,
      password: password
    });
    if (data == null || data === "") {
      throw new Error("No response body for login");
    }

    if (data.username != null) {
      DOMVariables.username.innerText = data.username;
    }
    else {
      DOMVariables.username.innerText = "이름 없음";
    }

    if (data.avatarFile != null) {
      DOMVariables.avatar.src = `/attach/${data.avatarFile}`;
    }

    if (data.email != null) {
      DOMVariables.email.innerText = data.username;
    }
    else {
      DOMVariables.email.innerText = "이메일 없음";
    }

    DOMVariables.id.innerText = data.id;

    const dateCreated = moment(data.created).toObject();
    DOMVariables.created.innerText = `${dateCreated.years}년 ${dateCreated.months}월 ${dateCreated.date}일 가입`;

    closeModalDirect("modal-login");
  }
  catch (error) {
    console.error(error);
    alert("로그인에 실패했습니다.");
  }
};

const getPostList = async () => {
  const { data } = await axios.get("/post");
  console.log(data);
  fetchPostList(data);
};

const addPost = async () => {
  const content = document.getElementById("post-input-content").value.trim();
  const userid = document.getElementById("user-id").innerText;

  const { data } = await axios.post("/post", {
    userid: userid,
    content: content,
    photoFile: await uploadFileAndGetId()
  });

  if (content === "") {
    alert("스토리를 업로드할 수 없습니다.");
    return;
  }

  if (data != null && data) {
    addPostList(data);
    document.getElementById("post-input-photo").value = "";
    document.getElementById("post-input-content").value = "";
    closeModalDirect("modal-write");
  }
  else {
    alert("스토리를 업로드할 수 없습니다.");
  }
};

const spawnPost = post => {
  if (post.id == null) {
    throw new Error("No post ID");
  }

  if (post.username == null) {
    post.username = "이름 없음";
  }

  if (post.created == null) {
    post.created = "날짜 없음";
  }

  if (post.content == null) {
    post.content = "내용 없음";
  }

  if (post.photoFile == null) {
    post.photoFile = "https://dummyimage.com/800x800/000/fff&text=dummy";
  }
  else {
    post.photoFile = `/attach/${post.photoFile}`;
  }

  return `
    <div class="box mt-base item-post" id="post-item-${post.id}">
      <div class="img-holder">
        <img class="img" src="${post.photoFile}" alt="">
      </div>
      <div class="mt-base item-post-data">
        <div class="text-secondary">${post.username}</div>
        <div class="text-secondary">${post.created}</div>
      </div>
      <div class="mt-base item-post-content">
        ${post.content}
      </div>
    </div>
  `;
}

const fetchPostList = posts => posts.forEach(post => document.getElementById("post-list").innerHTML += spawnPost(post));

const addPostList = post => document.getElementById("post-list").innerHTML += spawnPost(post);

const uploadFileAndGetId = async () => {
  let formData = new FormData();
  formData.append("file", document.getElementById("post-input-photo").files[0]);

  const { data } = await axios.post("/attach", formData, {
    headers: {
      "Content-Type": "multipart/form-data"
    }
  });

  return data.id;
};
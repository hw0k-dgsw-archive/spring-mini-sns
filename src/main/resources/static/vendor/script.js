const inDevelop = false;

const http = inDevelop
  ? axios.create({baseURL: "http://localhost:8080"})
  : axios;

document.addEventListener("DOMContentLoaded", () => {
  getPostListDOM();
});

const closeModal = btn => {
  const modalId = btn.attributes.getNamedItem("bind-for").value;
  document.getElementById("login-id").value = "";
  document.getElementById("login-password").value = "";
  document.getElementById("post-id").value = "";
  document.getElementById("post-input-photo").value = "";
  document.getElementById("post-input-content").value = "";
  document.getElementById(modalId).style.display = "none";
};

const openModal = btn => {
  const modalId = btn.attributes.getNamedItem("bind-for").value;
  document.getElementById(modalId).style.display = "flex";
};

const closeModalDirect = id => {
  document.getElementById("login-id").value = "";
  document.getElementById("login-password").value = "";
  document.getElementById("post-id").innerText = "";
  document.getElementById("post-input-photo").value = "";
  document.getElementById("post-input-content").value = "";
  document.getElementById(id).style.display = "none";
};

const openModalDirect = id => {
  document.getElementById(id).style.display = "flex";
};

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
    const { data } = await http.post("/user/login", {
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
      DOMVariables.avatar.src = `${inDevelop ? "http://localhost:8080" : ""}/attach/${data.avatarFile}`;
    }
    else {
      DOMVariables.avatar.src = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png";
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
    getPostListDOM();
  }
  catch (error) {
    console.error(error);
    alert("로그인에 실패했습니다.");
  }
};

const getPostListDOM = async () => {
  const { data } = await http.get("/post");
  fetchPostList(data);
};

const addOrUpdatePost = async () => {
  let userid = document.getElementById("user-id").innerText;
  let content = document.getElementById("post-input-content").value;
  const id = document.getElementById("post-id").innerText;

  const mode = id === "" ? "add" : "update";

  if (mode === "add") {
    const { data } = await http.post("/post", {
      userid: userid,
      content: content.trim(),
      photoFile: await uploadFileAndGetId()
    });

    if (content === "" || document.getElementById("post-input-photo").value == null) {
      alert("스토리를 업로드할 수 없습니다.");
      return;
    }

    if (data != null && data) {
      addPostToList(data);
      closeModalDirect("modal-write");
    }
    else {
      alert("스토리를 업로드할 수 없습니다.");
    }
  }
  else if (mode === "update") {
    const body = document.getElementById("post-input-photo").value === ""
    ? {
        content: content.trim()
    }
    : {
        content: content,
        photoFile: await uploadFileAndGetId()
    };

    const { data } = await http.put(`/post/${id}`, body);
    if (data != null && data) {
      replacePostDOM(data);
      closeModalDirect("modal-write");
    }
    else {
      alert("스토리를 업로드할 수 없습니다.");
    }
  }
};

const removePost = async id => {
  const { data } = http.delete(`/post/${id}`);
  if (data) {
    removePostDOMById(id);
  }
  else {
    alert("삭제에 실패했습니다.");
  }
};

const spawnPostDOM = post => {
  if (post.id == null) {
    throw new Error("No post ID");
  }

  if (post.username == null) {
    post.username = "이름 없음";
  }

  if (post.content == null) {
    post.content = "내용 없음";
  }

  if (post.photoFile == null) {
    post.photoFile = "https://dummyimage.com/800x800/000/fff&text=dummy";
  }
  else {
    post.photoFile = `${inDevelop ? "http://localhost:8080" : ""}/attach/${post.photoFile}`;
  }

  const dateObject = moment(post.created).toObject();
  const dateString = `${dateObject.years}년 ${dateObject.months}월 ${dateObject.date}일 작성`;

  const matchPostUserAndCurrent = post.userid === document.getElementById("user-id").innerText;

  return `
    <div class="box mt-base item-post" id="post-item-${post.id}">
      <div class="img-holder">
        <img class="img img-post" src="${post.photoFile}" alt="">
      </div>
      <div class="mt-base item-post-data">
        <div style="display: none;" id="post-by-${post.userid}"></div>
        <div class="text-secondary">${post.username}</div>
        <div class="text-secondary">${dateString}</div>
      </div>
      <div class="mt-base item-post-content">
        ${post.content}
      </div>
      ${ matchPostUserAndCurrent ? `
      <div class="mt-base item-post-content">
        <button class="box box-btn btn-blue" onclick="updatePostInit(${post.id})">수정</button>
        <button class="box box-btn btn-red" onclick="removePost(${post.id})">삭제</button>
      </div>
      ` : ""}
    </div>
  `;
};

const removePostDOMById = id => {
  const postDOM = document.getElementById(`post-item-${id}`);
  if (postDOM != null) {
    document.getElementById("post-list").removeChild(postDOM);
  }
};

const replacePostDOM = post => {
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
    post.photoFile = `${inDevelop ? "http://localhost:8080" : ""}/attach/${post.photoFile}`;
  }

  const matchPostUserAndCurrent = post.userid === document.getElementById("user-id").innerText;

  document.getElementById(`post-item-${post.id}`).innerHTML = `
    <div class="img-holder">
      <img class="img" src="${post.photoFile}" alt="">
    </div>
    <div class="mt-base item-post-data">
      <div style="display: none;" id="post-by-${post.userid}"></div>
      <div class="text-secondary">${post.username}</div>
      <div class="text-secondary">${post.created}</div>
    </div>
    <div class="mt-base item-post-content">
      ${post.content}
    </div>
    ${ matchPostUserAndCurrent ? `
    <div class="mt-base item-post-content">
      <button class="box box-btn btn-blue" onclick="updatePostInit(${post.id})" bind-for="modal-write">수정</button>
      <button class="box box-btn btn-red" onclick="removePost(${post.id})">삭제</button>
    </div>
    ` : ""}
  `;
};

const fetchPostList = posts => {
  document.getElementById("post-list").innerHTML = ``;
  posts.sort((a, b) => {
    if (moment(a.created).isBefore(b.created)) {
      return 1;
    }
    else if (moment(a.created).isAfter(b.created)) {
      return -1;
    }
    else if (moment(a.created).isSame(b.created)) {
      return 0;
    }
    return 0;
  });
  posts.forEach(post => document.getElementById("post-list").innerHTML += spawnPostDOM(post));
};

const addPostToList = post => document.getElementById("post-list").innerHTML = spawnPostDOM(post) + document.getElementById("post-list").innerHTML;

const uploadFileAndGetId = async () => {
  let formData = new FormData();
  formData.append("file", document.getElementById("post-input-photo").files[0]);

  const { data } = await http.post("/attach", formData, {
    headers: {
      "Content-Type": "multipart/form-data"
    }
  });

  return data.id;
};

const setPostData = async id => {
  const { data } = await http.get(`/post/${id}`);
  document.getElementById("post-id").innerText = data.id;
  document.getElementById("post-input-content").value = data.content;
};

const updatePostInit = async id => {
  await setPostData(id);
  openModalDirect("modal-write");
};
async function init(){
  const state = await (await fetch('/api/init')).json();
  render(state);
  console.log(`[SUCCESS] Initialized with ${state.total} questions`);
}

function render(s){
  document.getElementById('meta').textContent = `Question ${s.index+1} of ${s.total}`;
  document.getElementById('question').textContent = s.question;
  document.getElementById('prev').disabled = !s.hasPrevious;
  document.getElementById('next').disabled = !s.hasNext;
  document.getElementById('status').textContent = s.status || '';
  if(s.status) console.log(s.status + `: ${s.index+1}/${s.total}`);
}

async function next(){
  const state = await (await fetch('/api/next',{method:'POST'})).json();
  render(state);
}

async function previous(){
  const state = await (await fetch('/api/previous',{method:'POST'})).json();
  render(state);
}

document.addEventListener('DOMContentLoaded',()=>{
  document.getElementById('next').addEventListener('click',next);
  document.getElementById('prev').addEventListener('click',previous);
  init();
});

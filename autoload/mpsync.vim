if !has("python")
    echo "vim has to be compiled with +python to run this"
    finish
endif

if exists("g:markdown_preview_sync_loaded")
    finish
endif

let g:markdown_preview_sync_loaded = 1
let g:markdown_preview_sync_port = 7788

let s:plugin_root_dir = fnamemodify(resolve(expand("<sfile>:p")), ":h")

python << EOF
import sys
from os.path import normpath, join
import vim
plugin_root_dir = vim.eval('s:plugin_root_dir')
python_root_dir = normpath(join(plugin_root_dir, 'python'))
sys.path.insert(0, python_root_dir)
import java_vim_bridge
EOF

function! s:start()
    execute "silent !start /b java -jar \"" . s:plugin_root_dir . "\"/java/markdown-preview-sync.jar"

python <<EOF
port = vim.eval('g:markdown_preview_sync_port')
java_vim_bridge.start(int(port))
EOF
endfunction

function! s:is_start()
    return exists("g:markdown_preview_sync_start")
endfunction

function! s:open()
    if exists("g:markdown_preview_sync_chrome")
        execute "silent !start " . g:markdown_preview_sync_chrome . " --app=http://127.0.0.1:" . g:markdown_preview_sync_port . "/index"
    else
        echoerr "Not set browser path"
    endif
endfunction

function! s:sync()
python <<EOF
path = vim.eval('expand("%:p")')
lines = vim.eval('getline(1, line("$"))')
bottom = vim.eval('line("w$")')
java_vim_bridge.sync(path, lines, int(bottom))
EOF
endfunction

function! s:close()
python <<EOF
path = vim.eval('expand("%:p")')
java_vim_bridge.close(path)
EOF
endfunction

function! s:stop()
python <<EOF
path = vim.eval('expand("%:p")')
java_vim_bridge.stop()
EOF
endfunction

function! s:autocmd()
    augroup markdown_preview_sync
        autocmd!
        autocmd cursormoved,cursormovedi <buffer> call s:trigger_sync()
        autocmd bufwrite <buffer> call s:sync()
        autocmd vimleave * call s:trigger_stop()
    augroup end
endfunction

let b:old_current = 0
let b:old_bottom = 0
let b:old_last = 0

function! s:trigger_sync()
    let l:new_current = line(".")
    let l:new_bottom = line("w$")
    let l:new_last = line("$")
    if b:old_last ==# l:new_last
        if b:old_bottom > l:new_bottom
            if b:old_bottom - l:new_bottom >= 5
                call s:sync()
                let b:old_current = l:new_current
                let b:old_bottom = l:new_bottom
                let b:old_last = l:new_last
            endif
        elseif b:old_bottom < l:new_bottom
            call s:sync()
            let b:old_current = l:new_current
            let b:old_bottom = l:new_bottom
            let b:old_last = l:new_last
        else
        endif
    else
        call s:sync()
        let b:old_current = l:new_current
        let b:old_bottom = l:new_bottom
        let b:old_last = l:new_last
    endif


endfunction

function! s:trigger_stop()
    call s:close()
    call s:stop()
endfunction

function! mpsync#preview()
    if !s:is_start()
        call s:start()
        let g:markdown_preview_sync_start = 1
    endif
    call s:open()
    call s:autocmd()
endfunction

function! mpsync#close()
    if s:is_start()
        call s:close()
    endif
endfunction

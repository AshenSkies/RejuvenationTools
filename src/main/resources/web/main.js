
        function runCommand() {
            fetch('/run-command', { method: 'POST' })
                .then(response => response.text())
                .then(data => {
                    document.getElementById('output').innerText = data;
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        }
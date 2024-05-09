<nav class="navbar navbar-expand-lg navbar-light bg-light">
	<h3>Expense Calculator</h3>
	<div class="container">
		<div class="collapse navbar-collapse">
			<ul class="navbar-nav ml-auto" id="navbarPanel">
				
			</ul>

		</div>
	</div>
</nav>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        var userRole = sessionStorage.getItem('userRoleId');

        if (userRole === '1') {
            addAdminLinks();
        } else if (userRole === '2') {
            addGeneralUserLinks();
        }
    });

    function addAdminLinks() {
        var adminLinks = `
        	<li class="nav-item"><a class="nav-link" href="../admin/showhome">Home</a></li>
            <li class="nav-item"><a class="nav-link" href="../admin/showcategory">Category</a></li>
			<li class="nav-item ml-5 mr-3"><a class="nav-link btn btn-danger" href="../home/logout">Logout</a></li>
        `;
        appendLinks(adminLinks);
    }

    function addGeneralUserLinks() {
        var generalUserLinks = `
        	<li class="nav-item"><a class="nav-link" href="../guser/showhome">Home</a></li>
            <li class="nav-item"><a class="nav-link" href="../guser/showexpense">Expense</a></li>
            <li class="nav-item"><a class="nav-link" href="../guser/showincome">Income</a></li>
            <li class="nav-item"><a class="nav-link" href="../guser/showanalysis">Analysis</a></li>
			<li class="nav-item ml-5 mr-3"><a class="nav-link btn btn-danger" href="../home/logout">Logout</a></li>
        `;
        appendLinks(generalUserLinks);
    }

    function appendLinks(links) {
        var ul = document.getElementById("navbarPanel");
        ul.insertAdjacentHTML('beforeend', links);
    }

</script>

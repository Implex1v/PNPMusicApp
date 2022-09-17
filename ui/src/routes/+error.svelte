<script>
    import { page } from '$app/stores.js'
</script>

<div class="d-flex justify-content-center">
    <div>
        <h1 class="d-flex justify-content-center">{ $page.status }</h1>
        <p class="d-flex justify-content-center">{ $page.error.message }</p>
        <img src="https://http.cat/{ $page.status }" alt="cat">
    </div>
</div>

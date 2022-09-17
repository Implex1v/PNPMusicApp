<script>
    /** @type {import('./$types').Page} */
    export let data;
</script>

<svelte:head>
    <title>Song</title>
</svelte:head>

<h3>Song: {data.name}</h3>
<p>Song with id {data.id}</p>